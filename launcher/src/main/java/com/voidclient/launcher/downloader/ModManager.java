package com.voidclient.launcher.downloader;

import com.voidclient.launcher.utils.LauncherConfig;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class ModManager {

    private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

    public static void downloadAndInstallMod(String url, TextArea logArea) {
        logArea.appendText("[Mods] Baixando mod: " + url + "\n");
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(60))
                    .GET()
                    .build();
            HttpResponse<InputStream> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofInputStream());
            if (resp.statusCode() != 200) {
                logArea.appendText("[Mods] Falha ao baixar mod: status=" + resp.statusCode() + "\n");
                return;
            }
            Path modsFolder = LauncherConfig.getMinecraftFolder().resolve("mods");
            if (!Files.exists(modsFolder)) Files.createDirectories(modsFolder);
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            if (!fileName.endsWith(".jar")) fileName = fileName + ".jar";
            Path target = modsFolder.resolve(fileName);
            Files.copy(resp.body(), target, StandardCopyOption.REPLACE_EXISTING);
            logArea.appendText("[Mods] Mod instalado em: " + target.toAbsolutePath() + "\n");
        } catch (IOException | InterruptedException e) {
            logArea.appendText("[Mods] Erro ao baixar/instalar mod: " + e.getMessage() + "\n");
        }
    }
}
