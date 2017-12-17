package fxapp;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;

public class CodeCorrectionController implements IScreen {
    @FXML
    public TextField codeField;
    @FXML
    AnchorPane content;

    private ScreenSwitcher screenSwitcher;
    private int oldCaretPosition;

    public void initialize() {
        codeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                oldCaretPosition = codeField.getCaretPosition();
            }
        });
    }

    @Override
    public void onScreenExit() {

    }

    @Override
    public void onScreenShow() {

    }

    @Override
    public void setScreenSwitcher(ScreenSwitcher switcher) {
        this.screenSwitcher = switcher;
    }

    @Override
    public AnchorPane getContent() {
        return content;
    }

    public void setCode(String code) {
        Platform.runLater(() -> codeField.setText(code));
    }

    public String getCode() {
        return codeField.getText();
    }

    public void accept(MouseEvent mouseEvent) {
        //screenSwitcher.switchToScreen(ScreenSwitcher.Screens.SCANNING_SCREEN);
        screenSwitcher.comeBackToScanningScreen();
    }

    public void cancelClick(MouseEvent mouseEvent) {
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.SCANNING_SCREEN);
    }

    public void keyboardButtonClicked(MouseEvent mouseEvent) {
        Button source = (Button) mouseEvent.getSource();
        codeField.insertText(oldCaretPosition, source.getText());
    }

    public void backspaceClicked(MouseEvent mouseEvent) {
        if (oldCaretPosition > 0) {
            codeField.deleteText(oldCaretPosition - 1, oldCaretPosition);
        }
    }
}
