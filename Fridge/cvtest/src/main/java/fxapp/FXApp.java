package fxapp;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tools.CVUtils;
import tools.CameraFactory;

public class FXApp extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private ScreenSwitcher screenSwitcher;
    public static void main(String[] args) {
        CVUtils cvUtils = new CVUtils();//potrzebne do zaladowania bibliotek opencv
        Gson gson=new Gson();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Intelligent fridge");
        primaryStage.setOnCloseRequest(t -> {
            CameraFactory.release();
            Platform.exit();
            System.exit(0);
        });
        screenSwitcher=new ScreenSwitcher();
        initRootLayout();
        screenSwitcher.loadScreens();
        screenSwitcher.setRootPane(rootLayout);
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.START_SCREEN);
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
            //primaryStage.setFullScreen(true);
            primaryStage.show();
    }

}
