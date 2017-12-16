package fxapp;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class ProductListController implements IScreen{
    private ScreenSwitcher screenSwitcher;
    @FXML
    AnchorPane content;
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
        return null;
    }
}
