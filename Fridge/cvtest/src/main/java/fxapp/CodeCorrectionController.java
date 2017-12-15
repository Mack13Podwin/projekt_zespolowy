package fxapp;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CodeCorrectionController  implements IScreen{
    @FXML
    AnchorPane content;
    private String code;
    private ScreenSwitcher screenSwitcher;

    @Override
    public void onScreenExit() {

    }

    @Override
    public void onScreenShow() {

    }

    @Override
    public void setScreenSwitcher(ScreenSwitcher switcher) {
        this.screenSwitcher=switcher;
    }

    @Override
    public AnchorPane getContent() {
        return content;
    }
    public String getCorrectedCode(){
        return code;
    }
    public void setCodeToCorrect(String code){
        this.code=code;
    }

    public void accept(MouseEvent mouseEvent) {
        screenSwitcher.switchScreenBack(this);
    }
}
