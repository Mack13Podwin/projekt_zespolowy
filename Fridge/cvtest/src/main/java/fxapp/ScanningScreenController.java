package fxapp;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import tools.*;
import tools.http.Product;
import tools.http.ProductRequest;
import tools.http.ProductRequestCallback;
import tools.http.Response;


import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.*;

public class ScanningScreenController implements IScreen, IView, ProductRequestCallback {
    @FXML
    public Button nextButton;
    @FXML
    public Label barcodeStatusLabel;
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
    BarcodeUtils barcodeUtils=new BarcodeUtils();
    private ScheduledExecutorService timer;
    private ScreenSwitcher screenSwitcher;
    private StringProperty currentCodeProperty=new SimpleStringProperty();
    private Camera cam= CameraFactory.getInstance();
    private Alert requestAlert;
    //leave unused
    @Override
    public void setCurrentCode(String currentCode) {
        //leave unused
    }

    @Override
    public void setLastCode(String lastCode) {
        Platform.runLater(() -> currentCodeProperty.setValue(lastCode));
        setCurrentGreen();
    }

    @Override
    public void setCurrentRed() {
        Platform.runLater(()->currentLabel.setBackground(new Background(new BackgroundFill(Color
                .rgb(255, 0, 0), CornerRadii.EMPTY, Insets.EMPTY))));
    }

    @Override
    public void setCurrentGreen() {
        Platform.runLater(()->currentLabel.setBackground(new Background(new BackgroundFill(Color
                .rgb(0, 255, 0), CornerRadii.EMPTY, Insets.EMPTY))));
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
                CVUtils.flip(dst,frame);
                //CVUtils.rotate_bound(dst,frame,180);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date bestBefore=tessUtils.getDate(CVUtils.MatToBufferedImage(frame));
                Platform.runLater(()->{datePicker.setValue(bestBefore.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());});
                System.out.println(df.format((bestBefore)));

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
        nextButton.setDisable(true);
        ProductRequest productRequest;
        ProductRequest.RequestTypes requestType;
        switch (operationType){
            case NEW:
                requestType= ProductRequest.RequestTypes.NEW;
                break;
            case OPEN:
                requestType= ProductRequest.RequestTypes.OPEN;
                break;
            case DELETE:
                requestType= ProductRequest.RequestTypes.DELETE;
                break;
                default:
                    return;
        }
        productRequest = new ProductRequest(product,this, requestType);
        productRequest.start();
        requestAlert = new Alert(Alert.AlertType.INFORMATION);
        requestAlert.setTitle("Information Dialog");
        requestAlert.setHeaderText(null);
        requestAlert.getButtonTypes().clear();
        requestAlert.setContentText("Processing product");
        requestAlert.show();
    }

    @Override
    public void requestCallback(int statusCode, String response) {
        Platform.runLater(()->((Stage) requestAlert.getDialogPane().getScene().getWindow()).close());
        Platform.runLater(()->nextButton.setDisable(false));
        Gson gson = new Gson();
        Response resp= gson.fromJson(response,Response.class);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            //alert.getButtonTypes().clear();
            if(statusCode==200){
                String text="Product: "+((resp!=null)?resp.name:"")+" successfully processed";
                alert.setContentText(text);
            } else {
                alert.setContentText("Error was encountered during processing product, code:"+statusCode+" message:"+response);
            }
            alert.showAndWait();
        });

    }

    public void scanFruitClicked(MouseEvent mouseEvent) {
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.FRUIT_SCAN);
    }

    enum ScanOperationType{
        NEW,OPEN,DELETE
    }

    public ScanOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(ScanOperationType operationType)
    {
        String text=" BARCODE";
        this.operationType = operationType;
        switch (operationType){
            case OPEN:
                text="OPEN "+text;
                break;
            case NEW:
                text="NEW "+text;
                break;
            case DELETE:
                text="USED "+text;
                break;
        }
        barcodeStatusLabel.setText(text);
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
                Mat dst=new Mat();
                cam.captureFrame(frame);
                CVUtils.preprocess(frame, dst);
                BufferedImage img = CVUtils.MatToBufferedImage(dst);
                barcodeUtils.handleBarcodeImage(img,ScanningScreenController.this);
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
