package fxapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ActionChooserController implements IScreen{
    @FXML
    AnchorPane content;

    private ScreenSwitcher screenSwitcher;

    public void scanUsedClicked(MouseEvent mouseEvent) {
        screenSwitcher.switchToScanningScreen(this, ScanningScreenController.ScanOperationType.DELETE);
    }

    public void scanOpenClicked(MouseEvent mouseEvent) {
        screenSwitcher.switchToScanningScreen(this, ScanningScreenController.ScanOperationType.OPEN);
    }

    public void scanNewClicked(MouseEvent mouseEvent) {
        System.out.println(screenSwitcher==null);
        screenSwitcher.switchToScanningScreen(this, ScanningScreenController.ScanOperationType.NEW);
    }


    @Override
    public void onScreenExit() {

    }

    @Override
    public void onScreenShow() {

    }

    @Override
    public void setScreenSwitcher(ScreenSwitcher switcher) {
        screenSwitcher=switcher;
    }

    @Override
    public AnchorPane getContent() {
        return content;
    }

}
