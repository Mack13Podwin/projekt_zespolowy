package fxapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ScreenSwitcher {
    ArrayList<IScreen> pastScreens=new ArrayList<>();
    AnchorPane rootPane;
    HashMap<Screens,IScreen> screens=new HashMap<>();
    ScanningScreenController scanningScreenController;
    CodeCorrectionController codeCorrectionController;
    ActionChooserController actionChooserController;
    StartScreenController startScreenController;
    ProductListController productListController;
    FruitScanController fruitScanController;
    NewProductRequestScreen newProductRequestScreen;
    public ScanningScreenController.ScanOperationType getOperationType(){
        return scanningScreenController.getOperationType();
    }
    public String getCurrentBarcode(){
        return scanningScreenController.getCurrent();
    }
    public void setRootPane(AnchorPane rootPane) {
        this.rootPane = rootPane;
    }

    public void switchToScreen(Screens screen){
        rootPane.getChildren().clear();
        rootPane.getChildren().add(screens.get(screen).getContent());
        screens.get(screen).onScreenShow();
    }

    public void comeBackToScanningScreen() {
        scanningScreenController.setLastCode(codeCorrectionController.getCode());
        switchToScreen(Screens.SCANNING_SCREEN);
    }


    enum Screens {
        CODE_CORRECTION("CodeCorrection.fxml"), ACTION_CHOOSER("ActionChooser.fxml"), START_SCREEN("StartScreen.fxml"),
        PRODUCT_LIST("ProductList.fxml"),
        SCANNING_SCREEN("ScanningScreen.fxml"),
        FRUIT_SCAN("FruitScan.fxml"),
        NEW_PRODUCT_REQUEST("NewProductRequest.fxml");
        String filename;

        Screens(String filename) {
            this.filename = filename;
        }
    }
    public void loadScreens(){
        for (Screens screen:Screens.values()) {
            loadNewScreen(screen);
        }
        scanningScreenController=(ScanningScreenController) screens.get(Screens.SCANNING_SCREEN);
        startScreenController=(StartScreenController) screens.get(Screens.START_SCREEN);
        codeCorrectionController=(CodeCorrectionController) screens.get(Screens.CODE_CORRECTION);
        productListController=(ProductListController) screens.get(Screens.PRODUCT_LIST);
        actionChooserController=(ActionChooserController) screens.get(Screens.ACTION_CHOOSER);
        fruitScanController=(FruitScanController) screens.get(Screens.FRUIT_SCAN);
        newProductRequestScreen=(NewProductRequestScreen)screens.get(Screens.NEW_PRODUCT_REQUEST);
    }



    void switchToScanningScreen(IScreen exitedScreen, ScanningScreenController.ScanOperationType operationType) {
        ScanningScreenController controller=(ScanningScreenController) (screens.get(Screens.SCANNING_SCREEN));
        controller.setOperationType(operationType);
        //controller.setLastCode(((Integer)(ThreadLocalRandom.current().nextInt())).toString());
        switchToScreen(Screens.SCANNING_SCREEN);



    }
    void switchToCodeCorrection(){
        ScanningScreenController controller=(ScanningScreenController) (screens.get(Screens.SCANNING_SCREEN));
        controller.onScreenExit();
        CodeCorrectionController codeCorrectionController=(CodeCorrectionController)(screens.get(Screens.CODE_CORRECTION));
        //TODO verify if current or last
        codeCorrectionController.setCode(controller.getCurrent());
        switchToScreen(Screens.CODE_CORRECTION);
    }
    void switchBackToScanning(){

    }
    private FXMLLoader loadNewScreen(Screens screen) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXApp.class.getClassLoader().getResource(screen.filename));
        try {
            loader.load();
            screens.put(screen,loader.getController());
            ((IScreen)(loader.getController())).setScreenSwitcher(this);
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

}
