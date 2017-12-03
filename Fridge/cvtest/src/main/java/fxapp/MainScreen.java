package fxapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;
import tools.CVUtils;
import tools.Camera;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainScreen {
    @FXML
    private ImageView camPreview;
    @FXML
    private Button startButton;
    private ScheduledExecutorService timer;

    public void mouseClick(MouseEvent mouseEvent) {
        Camera cam=new Camera(0);
        Runnable frameGrabber = new Runnable() {

            @Override
            public void run()
            {
                // effectively grab and process a single frame
                Mat frame=new Mat();
                cam.captureFrame(frame);
                // convert and show the frame
                final Image img= CVUtils.MatToFXImage(frame);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        camPreview.setImage(img);
                    }
                });
            }
        };
        startButton.setDisable(true);
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
    }
}
