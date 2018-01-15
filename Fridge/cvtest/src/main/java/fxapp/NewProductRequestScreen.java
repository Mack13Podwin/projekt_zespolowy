package fxapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import tools.CVUtils;
import tools.Camera;
import tools.CameraFactory;
import tools.mail.Mail;

import javax.mail.MessagingException;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class NewProductRequestScreen implements IScreen{
    public ImageView scanningPreview1;
    public ImageView scanningPreview11;
    @FXML
    Button shootButton1;
    @FXML
    AnchorPane content;
    @FXML
    private ImageView scanningPreview;
    @FXML

    private Camera cam = CameraFactory.getInstance();
    private ScheduledExecutorService timer;
    private ScreenSwitcher screenSwitcher;
    private BufferedImage photo1=new BufferedImage(100,100,BufferedImage.TYPE_BYTE_GRAY);
    BufferedImage photo2;
    int currentPhoto=0;
    ScheduledFuture<?> previewFuture=null;
    public NewProductRequestScreen(){
//        Mat frame=new Mat();
//        cam.captureFrame(frame);
//        Mail mail=new Mail();
//        try {
//            mail.sendMail(CVUtils.MatToBufferedImage(frame));
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }
    public void startPreview(){

        Runnable frameGrabber = new Runnable() {

            @Override
            public void run()
            {
                // effectively grab and process a single frame
                Mat frame=new Mat();
                Mat dst=new Mat();
                cam.captureFrame(frame);
                CVUtils.preprocess(frame, dst);
                BufferedImage img = CVUtils.MatToBufferedImage(dst);
                //barcodeUtils.handleBarcodeImage(img,ScanningScreenController.this);
                // convert and show the frame
                final Image FXimg= CVUtils.MatToFXImage(frame);
                Platform.runLater(() -> scanningPreview.setImage(FXimg));
                frame.release();
                dst.release();
            }
        };

        this.timer = Executors.newSingleThreadScheduledExecutor();
        previewFuture=this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
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

    public void cancelClicked(MouseEvent mouseEvent) {
        onScreenExit();
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.START_SCREEN);
    }
    private void sendMail(){
        Mail mail=new Mail();
        try {
            mail.sendMail(photo1,photo2,screenSwitcher.getCurrentBarcode());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void acceptClicked(MouseEvent mouseEvent) {
        final Alert alert;
        alert=new Alert(Alert.AlertType.INFORMATION);
        Thread t=new Thread(() -> {


            sendMail();
            Platform.runLater(()->{
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                //alert.getButtonTypes().clear()
                alert.setContentText("Mail sent");
                alert.showAndWait();
            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Platform.runLater(() -> ((Stage) alert.getDialogPane().getScene().getWindow()).close());
        });
        t.start();
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.ACTION_CHOOSER);

    }

    public void shootClicked(MouseEvent mouseEvent) {
        Mat frame=new Mat();
        cam.captureFrame(frame);

        if(currentPhoto==0){
            photo1=CVUtils.MatToBufferedImage(frame);
            scanningPreview1.setImage(CVUtils.MatToFXImage(frame));
        } else {
            photo2=CVUtils.MatToBufferedImage(frame);
            scanningPreview11.setImage(CVUtils.MatToFXImage(frame));
        }
        System.out.println("frame set");
        frame.release();
        currentPhoto=(currentPhoto+1)%2;
    }
}
