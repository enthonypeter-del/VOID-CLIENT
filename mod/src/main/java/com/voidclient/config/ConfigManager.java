package com.voidclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private final Path configFile;
    private VoidConfig config;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigManager(Path configFile) {
        this.configFile = configFile;
        load();
    }

    public VoidConfig getConfig() {
        return config;
    }

    public void load() {
        try {
            if (Files.exists(configFile)) {
                String json = new String(Files.readAllBytes(configFile));
                config = gson.fromJson(json, VoidConfig.class);
            }
        } catch (IOException ignored) {
        }
        if (config == null) {
            config = VoidConfig.createDefault();
            save();
        }
    }

    public void save() {
        try {
            Files.createDirectories(configFile.getParent());
            Files.write(configFile, gson.toJson(config).getBytes());
        } catch (IOException ignored) {
        }
    }
}
