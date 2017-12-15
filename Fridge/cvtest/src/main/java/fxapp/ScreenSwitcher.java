package fxapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ScreenSwitcher {
    ArrayList<IScreen> pastScreens=new ArrayList<>();
    AnchorPane rootPane;
    HashMap<Screens,IScreen> screens;
    public void switchToScreen(Screens screen){
        rootPane.getChildren().clear();
        rootPane.getChildren().add(screens.get(screen).getContent());
    }
    public void switchToActionChooser(IScreen exitedScreen) {

    }

    enum Screens {
        CODE_CORRECTION("CodeCorrection.fxml"), ACTION_CHOOSER("ActionChooser.fxml"), START_SCREEN("StartScreen.fxml"),
        PRODUCT_LIST("ProductList.fxml"),
        SCANNING_SCREEN("ScanningScreen.fxml");
        String filename;

        Screens(String filename) {
            this.filename = filename;
        }
    }
    public void loadScreens(){
        for (Screens screen:Screens.values()) {
            loadNewScreen(screen);
        }
    }
    //called by scanning screen
    void  switchToCodeCorrection(IScreen exitedScreen) {
        //FXMLLoader loader = loadNewScreen(exitedScreen, Screens.CODE_CORRECTION);
    }

    void switchToScanningScreen(IScreen exitedScreen, ScanningScreenController.ScanOperationType operationType) {
        //FXMLLoader loader = loadNewScreen(exitedScreen, Screens.SCANNING_SCREEN);
        //ScanningScreenController controller=loader.getController();
        //controller.setOperationType(operationType);
        //controller.onScreenShow();
    }

    private FXMLLoader loadNewScreen(Screens screen) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXApp.class.getClassLoader().getResource(screen.filename));
        try {
            loader.load();
            screens.put(screen,loader.getController());
            ((IScreen)loader.getController()).setScreenSwitcher(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            rootPane.getChildren().clear();
            rootPane.getChildren().add((AnchorPane) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return loader;
    }
    void switchScreenBack(IScreen currentScreen){
        AnchorPane pane=(AnchorPane)(currentScreen.getContent().getParent());
        rootPane.getChildren().clear();
        rootPane.getChildren().add(pastScreens.get(pastScreens.size()-1).getContent());
        pastScreens.remove(pastScreens.size()-1);
    }
}
