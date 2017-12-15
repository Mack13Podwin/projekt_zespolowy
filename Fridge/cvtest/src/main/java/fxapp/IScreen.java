package fxapp;

import javafx.scene.layout.AnchorPane;

public interface IScreen {
    void onScreenExit();
    void onScreenShow();
    void setScreenSwitcher(ScreenSwitcher switcher);
    AnchorPane getContent();
}
