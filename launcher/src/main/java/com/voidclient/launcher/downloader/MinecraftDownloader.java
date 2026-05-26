package com.voidclient.launcher.downloader;

import com.voidclient.launcher.utils.LauncherConfig;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MinecraftDownloader {

    public static void verifyVersion(String version, TextArea logArea) {
        logArea.appendText("[Downloader] Verificando versão " + version + "...\n");
        Path minecraftFolder = LauncherConfig.getMinecraftFolder();
        Path versionFolder = minecraftFolder.resolve("versions").resolve(version);
        if (Files.exists(versionFolder)) {
            logArea.appendText("[Downloader] Versão já presente localmente.\n");
        } else {
            logArea.appendText("[Downloader] Pasta de versão ausente. Baixando arquivos necessários...\n");
            logArea.appendText("[Downloader] Este projeto inclui o sistema de download básico, mas o arquivo oficial deve ser obtido via infraestrutura Mojang.\n");
            try {
                Files.createDirectories(versionFolder);
                Files.write(versionFolder.resolve(version + ".json"), LauncherConfig.defaultVersionJson(version).getBytes());
                logArea.appendText("[Downloader] Estrutura de versão criada.\n");
            } catch (IOException e) {
                logArea.appendText("[Downloader] Falha ao criar estrutura de versão: " + e.getMessage() + "\n");
            }
        }
    }

    public static void launchMinecraft(String version, String profileName, TextArea logArea) {
        logArea.appendText("[Launcher] Iniciando Minecraft " + version + " com perfil " + profileName + "...\n");
        logArea.appendText("[Launcher] Certifique-se de ter instalado o Forge 1.8.9 e copiado o mod Void Client para a pasta mods.\n");

        try {
            ProcessBuilder builder = new ProcessBuilder(
                System.getProperty("java.home") + File.separator + "bin" + File.separator + "java",
                "-Xmx2G",
                "-cp",
                buildMinecraftClasspath(version),
                "net.minecraft.launchwrapper.Launch",
                "--username", profileName.isEmpty() ? "VoidPlayer" : profileName,
                "--version", "VoidClient-" + version,
                "--gameDir", LauncherConfig.getMinecraftFolder().toAbsolutePath().toString(),
                "--assetsDir", LauncherConfig.getMinecraftFolder().resolve("assets").toAbsolutePath().toString(),
                "--assetIndex", version,
                "--uuid", "00000000-0000-0000-0000-000000000000",
                "--accessToken", "0"
            );

            builder.directory(LauncherConfig.getMinecraftFolder().toFile());
            builder.inheritIO();
            Process process = builder.start();
            logArea.appendText("[Launcher] Minecraft iniciado.\n");
        } catch (IOException e) {
            logArea.appendText("[Launcher] Erro ao iniciar Minecraft: " + e.getMessage() + "\n");
        }
    }

    private static String buildMinecraftClasspath(String version) {
        File minecraftFolder = LauncherConfig.getMinecraftFolder().toFile();
        File versionFolder = new File(minecraftFolder, "versions" + File.separator + version);
        File jarFile = new File(versionFolder, version + ".jar");
        File modFile = new File(minecraftFolder, "mods" + File.separator + "voidclient.jar");
        return jarFile.getAbsolutePath() + File.pathSeparator + modFile.getAbsolutePath();
    }
}
