package fxapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tools.CVUtils;

import java.io.IOException;

public class FXApp extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;

    public static void main(String[] args) {
        CVUtils cvUtils = new CVUtils();//potrzebne do zalatania
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        initRootLayout();
    }

    public Stage getPrimaryStage() {
        return primaryStage;

    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FXApp.class.getClassLoader().getResource("MainScreen.fxml"));
            rootLayout = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
