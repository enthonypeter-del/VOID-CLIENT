package com.voidclient.launcher;

import com.voidclient.launcher.ui.LaunchView;
import com.voidclient.launcher.utils.LauncherConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class VoidLauncherApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LauncherConfig.ensureConfigExists();
        LaunchView launchView = new LaunchView(primaryStage);
        Scene scene = new Scene(launchView.createRoot(), 980, 640);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

        primaryStage.setTitle("Void Client Launcher");
        primaryStage.getIcons().add(createIcon());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Image createIcon() {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(64, 64);
        javafx.scene.canvas.GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(javafx.scene.paint.Color.web("#0f0f0f"));
        context.fillRect(0, 0, 64, 64);
        context.setStroke(javafx.scene.paint.Color.web("#ff2b2b"));
        context.setLineWidth(6);
        context.strokeOval(6, 6, 52, 52);
        context.setStroke(javafx.scene.paint.Color.web("#ffffff"));
        context.setLineWidth(5);
        context.strokeLine(22, 18, 42, 32);
        context.strokeLine(42, 32, 22, 46);
        return canvas.snapshot(null, null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
