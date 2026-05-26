package com.voidclient.launcher.auth;

import com.voidclient.launcher.utils.LauncherConfig;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

public class MicrosoftAuthService {

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    public static void openLogin(Stage stage, TextArea logArea, TextField profileField) {
        logArea.appendText("[Auth] Iniciando Device Code flow da Microsoft...\n");

        String clientId = LauncherConfig.getMicrosoftClientId();
        if (clientId == null || clientId.isBlank()) {
            logArea.appendText("[Auth] microsoftClientId não configurado. Edite o arquivo de configuração em ~/.voidclient/launcher-config.json e adicione seu clientId.\n");
            return;
        }

        // Start the device code flow in a background thread
        new Thread(() -> {
            try {
                Map<String, String> device = requestDeviceCode(clientId, logArea);
                if (device == null) {
                    logArea.appendText("[Auth] Falha ao solicitar device code.\n");
                    return;
                }

                String userCode = device.get("user_code");
                String verificationUri = device.get("verification_uri");
                String deviceCode = device.get("device_code");
                int interval = Integer.parseInt(device.getOrDefault("interval", "5"));

                Platform.runLater(() -> logArea.appendText("[Auth] Abra " + verificationUri + " e digite o código: " + userCode + "\n"));

                String accessToken = pollForToken(clientId, deviceCode, interval, logArea);
                if (accessToken == null) {
                    Platform.runLater(() -> logArea.appendText("[Auth] Falha ao obter access token.\n"));
                    return;
                }

                Platform.runLater(() -> logArea.appendText("[Auth] Token Microsoft obtido. Iniciando autenticação Xbox/Minecraft...\n"));

                String mcAccessToken = exchangeToMinecraftToken(accessToken, logArea);
                if (mcAccessToken == null) {
                    Platform.runLater(() -> logArea.appendText("[Auth] Falha ao obter token Minecraft.\n"));
                    return;
                }

                String username = fetchMinecraftProfile(mcAccessToken, logArea);
                if (username != null) {
                    LauncherConfig.saveLastProfile(username);
                    Platform.runLater(() -> {
                        profileField.setText(username);
                        logArea.appendText("[Auth] Logado como: " + username + "\n");
                    });
                }

            } catch (Exception e) {
                Platform.runLater(() -> logArea.appendText("[Auth] Erro no fluxo de autenticação: " + e.getMessage() + "\n"));
            }
        }, "msft-device-flow-thread").start();
    }

    private static Map<String, String> requestDeviceCode(String clientId, TextArea logArea) throws IOException, InterruptedException {
        String body = "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode("XboxLive.signin offline_access", StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://login.microsoftonline.com/consumers/oauth2/v2.0/devicecode"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            Platform.runLater(() -> logArea.appendText("[Auth] requestDeviceCode status=" + resp.statusCode() + " body=" + resp.body() + "\n"));
            return null;
        }

        // very small JSON parse without adding dependency
        String s = resp.body();
        return Map.of(
                "device_code", extractJson(s, "device_code"),
                "user_code", extractJson(s, "user_code"),
                "verification_uri", extractJson(s, "verification_uri"),
                "interval", extractJson(s, "interval")
        );
    }

    private static String pollForToken(String clientId, String deviceCode, int interval, TextArea logArea) throws IOException, InterruptedException {
        String tokenUrl = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token";
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 15 * 60 * 1000) { // 15 minutes
            String body = "grant_type=device_code&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                    + "&device_code=" + URLEncoder.encode(deviceCode, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .timeout(Duration.ofSeconds(20))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> resp = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200) {
                String at = extractJson(resp.body(), "access_token");
                return at;
            } else {
                String error = extractJson(resp.body(), "error");
                if (error != null && (error.equals("authorization_pending") || error.equals("authorization_declined") || error.equals("slow_down"))) {
                    Thread.sleep(interval * 1000L);
                    continue;
                } else {
                    Platform.runLater(() -> logArea.appendText("[Auth] token error: " + resp.body() + "\n"));
                    return null;
                }
            }
        }
        return null;
    }

    private static String exchangeToMinecraftToken(String msAccessToken, TextArea logArea) throws IOException, InterruptedException {
        // 1) Authenticate with Xbox Live
        String xblBody = "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"d=" + msAccessToken + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}";

        HttpRequest xblReq = HttpRequest.newBuilder()
                .uri(URI.create("https://user.auth.xboxlive.com/user/authenticate"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(20))
                .POST(HttpRequest.BodyPublishers.ofString(xblBody))
                .build();

        HttpResponse<String> xblResp = HTTP.send(xblReq, HttpResponse.BodyHandlers.ofString());
        if (xblResp.statusCode() != 200) {
            Platform.runLater(() -> logArea.appendText("[Auth] XBL auth failed: " + xblResp.body() + "\n"));
            return null;
        }

        String xblToken = extractJson(xblResp.body(), "Token");
        String userHash = extractJson(xblResp.body(), "DisplayClaims"); // crude, not extracting nested properly

        // 2) XSTS
        String xstsBody = "{\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\",\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"" + xblToken + "\"]}}";
        HttpRequest xstsReq = HttpRequest.newBuilder()
                .uri(URI.create("https://xsts.auth.xboxlive.com/xsts/authorize"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(20))
                .POST(HttpRequest.BodyPublishers.ofString(xstsBody))
                .build();

        HttpResponse<String> xstsResp = HTTP.send(xstsReq, HttpResponse.BodyHandlers.ofString());
        if (xstsResp.statusCode() != 200) {
            Platform.runLater(() -> logArea.appendText("[Auth] XSTS failed: " + xstsResp.body() + "\n"));
            return null;
        }

        String xstsToken = extractJson(xstsResp.body(), "Token");
        String uhs = extractJson(xstsResp.body(), "DisplayClaims");

        // 3) Minecraft login with Xbox
        String mcBody = "{\"identityToken\":\"XBL3.0 x=" + uhs + ";\"}";
        HttpRequest mcReq = HttpRequest.newBuilder()
                .uri(URI.create("https://api.minecraftservices.com/authentication/login_with_xbox"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(20))
                .POST(HttpRequest.BodyPublishers.ofString(mcBody))
                .build();

        HttpResponse<String> mcResp = HTTP.send(mcReq, HttpResponse.BodyHandlers.ofString());
        if (mcResp.statusCode() != 200) {
            Platform.runLater(() -> logArea.appendText("[Auth] Minecraft login failed: " + mcResp.body() + "\n"));
            return null;
        }

        String mcAccessToken = extractJson(mcResp.body(), "access_token");
        return mcAccessToken;
    }

    private static String fetchMinecraftProfile(String mcAccessToken, TextArea logArea) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.minecraftservices.com/minecraft/profile"))
                .header("Authorization", "Bearer " + mcAccessToken)
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        HttpResponse<String> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            Platform.runLater(() -> logArea.appendText("[Auth] profile fetch failed: " + resp.body() + "\n"));
            return null;
        }

        return extractJson(resp.body(), "name");
    }

    private static String extractJson(String json, String key) {
        if (json == null) return null;
        String pattern = "\"" + key + "\"\s*:\s*\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return null;
        int start = idx + pattern.length();
        int end = json.indexOf('"', start);
        if (end == -1) return null;
        return json.substring(start, end);
    }
}
