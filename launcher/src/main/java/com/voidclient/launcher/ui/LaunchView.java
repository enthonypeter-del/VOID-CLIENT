package com.voidclient.launcher.ui;

import com.voidclient.launcher.auth.MicrosoftAuthService;
import com.voidclient.launcher.downloader.MinecraftDownloader;
import com.voidclient.launcher.utils.LauncherConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LaunchView {

    private final Stage stage;
    private final TextField profileField;
    private final TextField modUrlField;
    private final ComboBox<String> versionBox;
    private final TextArea logArea;

    public LaunchView(Stage stage) {
        this.stage = stage;
        profileField = new TextField();
        versionBox = new ComboBox<>();
        logArea = new TextArea();
        modUrlField = new TextField();
    }

    public Node createRoot() {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createCenter());
        root.setBottom(createFooter());
        root.setStyle("-fx-background-color: #101010;");
        return root;
    }

    private Node createHeader() {
        HBox header = new HBox(12);
        header.setPadding(new Insets(24));
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(createLogo(), new Label("Void Client Launcher"));
        header.setStyle("-fx-background-color: linear-gradient(to right, #1c0303, #220a0a, #1a1a1a);");
        return header;
    }

    private Node createLogo() {
        Image logo = new Image(getClass().getResourceAsStream("/icon.png"), 56, 56, true, true);
        ImageView logoView = new ImageView(logo);
        logoView.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.35), 10, 0.2, 0, 0);");
        return logoView;
    }

    private Node createCenter() {
        VBox center = new VBox(16);
        center.setPadding(new Insets(24));

        profileField.setPromptText("E-mail ou nome do perfil Microsoft");
        versionBox.getItems().addAll("1.8.9");
        versionBox.setValue("1.8.9");

        Button authButton = new Button("Login Microsoft");
        authButton.getStyleClass().add("red-button");
        authButton.setOnAction(e -> MicrosoftAuthService.openLogin(stage, logArea, profileField));

        Button downloadButton = new Button("Verificar e baixar 1.8.9");
        downloadButton.getStyleClass().add("red-button");
        downloadButton.setOnAction(e -> MinecraftDownloader.verifyVersion(versionBox.getValue(), logArea));

        Button launchButton = new Button("Iniciar Minecraft 1.8.9");
        launchButton.getStyleClass().add("red-button");
        launchButton.setOnAction(e -> MinecraftDownloader.launchMinecraft(versionBox.getValue(), profileField.getText(), logArea));

        Button installModButton = new Button("Instalar mod (URL)");
        installModButton.getStyleClass().add("red-button");
        installModButton.setOnAction(e -> com.voidclient.launcher.downloader.ModManager.downloadAndInstallMod(modUrlField.getText(), logArea));

        center.getChildren().addAll(createField("Perfil de jogo", profileField), createField("Versão", versionBox), authButton, downloadButton, launchButton, createField("Mod URL", modUrlField), installModButton, createLogPane());
        return center;
    }

    private Node createField(String label, Node field) {
        VBox box = new VBox(8);
        Label text = new Label(label);
        text.getStyleClass().add("field-label");
        box.getChildren().addAll(text, field);
        return box;
    }

    private Node createLogPane() {
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefHeight(220);
        return logArea;
    }

    private Node createFooter() {
        Label versionText = new Label("Void Client Launcher • Red/Black PvP 1.8.9");
        versionText.getStyleClass().add("footer-label");
        HBox footer = new HBox(versionText);
        footer.setPadding(new Insets(16));
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: #111111;");
        return footer;
    }
}
