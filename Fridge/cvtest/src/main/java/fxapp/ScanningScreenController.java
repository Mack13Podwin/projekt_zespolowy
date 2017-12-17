package fxapp;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import tools.*;
import tools.http.ProductRequest;
import tools.http.ProductRequestCallback;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.*;

public class ScanningScreenController implements IScreen, IView, ProductRequestCallback {
    @FXML
    public Button nextButton;
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
    @FXML
    private DatePicker datePicker;
    private ScheduledExecutorService timer;
    private ScreenSwitcher screenSwitcher;
    private StringProperty currentCodeProperty=new SimpleStringProperty();
    private Camera cam= CameraFactory.getInstance();
    private Alert requestAlert;
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.getButtonTypes().clear();
        alert.setContentText("Preprocessing Date");
        Thread t= new Thread(() -> {
            try {

                Mat frame = new Mat();
                Mat dst = new Mat();
                cam.captureFrame(frame);
                System.out.println("OK scanned");
                CVUtils.preprocess(frame, dst);
                System.out.println((tessUtils.getText(CVUtils.MatToBufferedImage(dst))));

            } catch (Throwable throwable){
                System.out.println(throwable.getLocalizedMessage()+'\n');
                throwable.printStackTrace();
            } finally {
                Platform.runLater(()->((Stage) alert.getDialogPane().getScene().getWindow()).close());
                Platform.runLater(()->dateButton.setDisable(false));
            }

        });
        t.start();
        alert.show();


    }

    public void nextClicked(MouseEvent mouseEvent) {
        Date date=Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Product product=new Product(currentCodeProperty.get(),date);
        switch (operationType){
            case NEW:
                nextButton.setDisable(true);
                ProductRequest productRequest =new ProductRequest(product,this, ProductRequest.RequestTypes.NEW);
                productRequest.start();

                requestAlert = new Alert(Alert.AlertType.INFORMATION);
                requestAlert.setTitle("Information Dialog");
                requestAlert.setHeaderText(null);
                requestAlert.getButtonTypes().clear();
                requestAlert.setContentText("Processing product");
                requestAlert.show();
                break;
        }
    }

    @Override
    public void requestCallback(int statusCode, String response) {
        Platform.runLater(()->((Stage) requestAlert.getDialogPane().getScene().getWindow()).close());
        Platform.runLater(()->nextButton.setDisable(false));
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            //alert.getButtonTypes().clear();
            if(statusCode==200){
                alert.setContentText("Product successfully processed");
            } else {
                alert.setContentText("Error was encountered during processing product, code:"+statusCode+" message:"+response);
            }
            alert.showAndWait();
        });

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
        LocalDate date=LocalDate.now().plusDays(1);
        datePicker.setValue(date);
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
