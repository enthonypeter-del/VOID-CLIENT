package com.voidclient.launcher.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LauncherConfig {

    public static Path getMinecraftFolder() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, ".minecraft");
    }

    public static void ensureConfigExists() {
        try {
            Path configFolder = Paths.get(System.getProperty("user.home"), ".voidclient");
            if (!Files.exists(configFolder)) {
                Files.createDirectories(configFolder);
            }
            Path configFile = configFolder.resolve("launcher-config.json");
            if (!Files.exists(configFile)) {
                Files.write(configFile, defaultConfig().getBytes());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String defaultConfig() {
        return "{\n" +
            "  \"lastVersion\": \"1.8.9\",\n" +
            "  \"lastProfile\": \"VoidPlayer\",\n" +
            "  \"useOfficialAuth\": true,\n" +
            "  \"microsoftClientId\": \"\"\n" +
            "}\n";
    }

    public static String getMicrosoftClientId() {
        try {
            Path configFile = Paths.get(System.getProperty("user.home"), ".voidclient", "launcher-config.json");
            if (!Files.exists(configFile)) return null;
            String json = Files.readString(configFile);
            String key = "\"microsoftClientId\"";
            int idx = json.indexOf(key);
            if (idx == -1) return null;
            int colon = json.indexOf(':', idx);
            int firstQuote = json.indexOf('"', colon + 1);
            int secondQuote = json.indexOf('"', firstQuote + 1);
            return json.substring(firstQuote + 1, secondQuote);
        } catch (IOException e) {
            return null;
        }
    }

    public static void saveLastProfile(String profile) {
        try {
            Path configFile = Paths.get(System.getProperty("user.home"), ".voidclient", "launcher-config.json");
            if (!Files.exists(configFile)) return;
            String json = Files.readString(configFile);
            if (json.contains("\"lastProfile\"")) {
                json = json.replaceAll("(\\\"lastProfile\\\"\\s*:\\s*\\\")[^\\\"]*(\\\")", "$1" + profile + "$2");
            }
            Files.writeString(configFile, json);
        } catch (IOException ignored) {
        }
    }

    public static String defaultVersionJson(String version) {
        return "{\n" +
            "  \"id\": \"" + version + "\",\n" +
            "  \"time\": \"2020-01-01T00:00:00+00:00\",\n" +
            "  \"releaseTime\": \"2020-01-01T00:00:00+00:00\",\n" +
            "  \"type\": \"release\",\n" +
            "  \"minecraftArguments\": \"--username ${auth_player_name} --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token}\",\n" +
            "  \"mainClass\": \"net.minecraft.client.main.Main\",\n" +
            "  \"assets\": \"1.8.9\",\n" +
            "  \"libraries\": []\n" +
            "}\n";
    }
}
