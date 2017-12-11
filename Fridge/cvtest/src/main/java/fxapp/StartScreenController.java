package fxapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StartScreenController {
    @FXML    AnchorPane content;
    public void startButtonClicked(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXApp.class.getClassLoader().getResource("2.fxml"));
        try {
            content.getChildren().setAll((AnchorPane)loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
