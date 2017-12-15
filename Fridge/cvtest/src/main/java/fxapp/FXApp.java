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
    private ScreenSwitcher screenSwitcher;
    public static void main(String[] args) {
        CVUtils cvUtils = new CVUtils();//potrzebne do zaladowania bibliotek opencv
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Intelligent fridge");
        screenSwitcher=new ScreenSwitcher();
        initRootLayout();
        screenSwitcher.loadScreens();
    }



    public Stage getPrimaryStage() {
        return primaryStage;

    }

    public void initRootLayout() {
            // Load root layout from fxml file.
           // FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(FXApp.class.getClassLoader().getResource("StartScreen.fxml"));
            //rootLayout =  loader.load();
            //ScreenSwitcher.rootPane=rootLayout;
            // Show the scene containing the root layout.
            rootLayout=new AnchorPane();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
    }

}
