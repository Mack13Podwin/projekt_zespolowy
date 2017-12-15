package fxapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StartScreenController implements IScreen{
    @FXML    AnchorPane content;
    public void startButtonClicked(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXApp.class.getClassLoader().getResource("ActionChooser.fxml"));
        try {
            content.getChildren().setAll((AnchorPane)loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScreenExit() {

    }

    @Override
    public void onScreenShow() {

    }

    @Override
    public void setScreenSwitcher(ScreenSwitcher switcher) {

    }

    @Override
    public AnchorPane getContent() {
        return content;
    }


}
