package fxapp;

import com.sun.org.apache.xml.internal.utils.StringBufferPool;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.opencv.core.Mat;
import tools.*;


import java.io.IOException;
import java.util.concurrent.*;

public class ScanningScreenController implements IScreen, IView{
    TessUtils tessUtils=new TessUtils();
    @FXML
    private ImageView scanningPreview;
    @FXML
    private AnchorPane content;
    @FXML
    private Label currentLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button dateButton;
    private ScheduledExecutorService timer;
    private ScreenSwitcher screenSwitcher;
    private StringProperty currentCodeProperty=new SimpleStringProperty();
    private Camera cam= CameraFactory.getInstance();
    @Override
    public void setCurrentCode(String currentCode) {
        Platform.runLater(() -> currentCodeProperty.setValue(currentCode));
    }

    @Override
    public void setLastCode(String lastCode) {

    }

    @Override
    public void setCurrentRed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrentGreen() {
        throw  new UnsupportedOperationException();
    }

    @Override
    public String getCurrent() {
        return currentLabel.getText();
    }
    public StringProperty getCurrentCodeProperty(){
        return currentCodeProperty;
    }

    public void dateClicked(MouseEvent mouseEvent) {
        dateButton.setDisable(true);
        Thread t=new Thread(){
            @Override public void run(){
                Mat frame=new Mat();
                Mat dst=new Mat();
                cam.captureFrame(frame);
                CVUtils.preprocess(frame,dst);
                System.out.println((tessUtils.getText(CVUtils.MatToBufferedImage(dst))));
            }
        };
        t.start();
    }

    enum ScanOperationType{
        NEW,OPEN,DELETE
    }

    public ScanOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(ScanOperationType operationType) {
        this.operationType = operationType;
    }
    public void initialize(){
        currentCodeProperty.bindBidirectional(currentLabel.textProperty());
    }
    ScanOperationType operationType;



    ScheduledFuture<?> previewFuture=null;
    public void startPreview(){

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
                        scanningPreview.setImage(img);
                    }
                });
                frame.release();
            }
        };

        this.timer = Executors.newSingleThreadScheduledExecutor();
        previewFuture=this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
    }


    public void cancelClicked(MouseEvent mouseEvent) {
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.ACTION_CHOOSER);

    }

    public void fixClicked(MouseEvent mouseEvent) {
        screenSwitcher.switchToCodeCorrection();
    }

    @Override
    public void onScreenExit() {
        if(previewFuture!=null){
            previewFuture.cancel(false);
        }
        previewFuture=null;
    }

    @Override
    public void onScreenShow() {
        startPreview();
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
