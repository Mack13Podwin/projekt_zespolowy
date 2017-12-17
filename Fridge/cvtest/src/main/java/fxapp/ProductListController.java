package fxapp;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
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
    public void setProductList(ObservableList list){

    }
    private void fetchProductList(){

    }
    @Override
    public void setScreenSwitcher(ScreenSwitcher switcher) {
        screenSwitcher=switcher;
    }

    @Override
    public AnchorPane getContent() {
        return content;
    }

    public void cancelClicked(MouseEvent mouseEvent) {
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.ACTION_CHOOSER);
    }
}
