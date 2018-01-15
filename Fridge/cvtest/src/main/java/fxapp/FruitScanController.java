package fxapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.opencv.core.Mat;
import tools.CVUtils;
import tools.Camera;
import tools.CameraFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import tools.http.*;

public class FruitScanController implements IScreen, FruitRequestCallback,ProductRequestCallback {
    public static ArrayList<String> fruits = new ArrayList<>(Arrays.asList("banana", "mandarin", "apple", "grapefruit", "orange", "lemon", "kiwi"));
    String currentFruit = null;
    @FXML
    private AnchorPane content;
    @FXML
    private Button scanFruit;
    @FXML
    private Button nextButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label barcodeStatusLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ImageView scanningPreview;
    private Camera cam = CameraFactory.getInstance();
    private ScheduledExecutorService timer;
    private ScreenSwitcher screenSwitcher;
    Alert alert;

    @Override
    public void onScreenExit() {
        if (previewFuture != null) {
            previewFuture.cancel(false);
        }
        previewFuture = null;
    }

    @Override
    public void onScreenShow() {
        startPreview();
    }

    @Override
    public void setScreenSwitcher(ScreenSwitcher switcher) {
        screenSwitcher = switcher;
    }

    @Override
    public AnchorPane getContent() {
        return content;
    }

    public void cancelClicked(MouseEvent mouseEvent) {
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.SCANNING_SCREEN);
    }

    public void nextClicked(MouseEvent mouseEvent) {
        Date date=Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Product product=new Product(currentFruit,date);
        nextButton.setDisable(true);
        ProductRequest productRequest=null;
        switch (screenSwitcher.getOperationType()){
            case NEW:
                productRequest = new ProductRequest(product,this, ProductRequest.RequestTypes.NEW);
            break;
            case DELETE:
                productRequest = new ProductRequest(product,this, ProductRequest.RequestTypes.DELETE);
                break;
            case OPEN:
                productRequest = new ProductRequest(product,this, ProductRequest.RequestTypes.OPEN);
                break;
        }
        productRequest.start();
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.getButtonTypes().clear();
        alert.setContentText("Processing product");
        alert.show();
    }

    public void initialize() {
        LocalDate date = LocalDate.now().plusDays(1);
        datePicker.setValue(date);
    }

    ScheduledFuture<?> previewFuture = null;

    public void startPreview() {

        Runnable frameGrabber = new Runnable() {

            @Override
            public void run() {
                // effectively grab and process a single frame
                Mat frame = new Mat();
                Mat dst = new Mat();
                cam.captureFrame(frame);
                // convert and show the frame
                final Image FXimg = CVUtils.MatToFXImage(frame);
                Platform.runLater(() -> scanningPreview.setImage(FXimg));
                frame.release();
                dst.release();
            }
        };

        this.timer = Executors.newSingleThreadScheduledExecutor();
        previewFuture = this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
    }


    public void scanClick(MouseEvent mouseEvent) {
        scanFruit.setDisable(true);
        Mat frame = new Mat();
        Mat dst = new Mat();
        cam.captureFrame(frame);
        BufferedImage buf = CVUtils.MatToBufferedImage(frame);
        frame.release();
        FruitRequest fruitRequest = new FruitRequest(buf, this);
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.getButtonTypes().clear();
        alert.setContentText("Processing fruit");
        alert.show();
        fruitRequest.start();
    }

    //if error true then error description is passed as fruitName
    @Override
    public void fruitRequestCallback(boolean error, String fruitName) {
        Platform.runLater(() -> ((Stage) alert.getDialogPane().getScene().getWindow()).close());
        Platform.runLater(()->{
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.getButtonTypes().clear();
        }
        );
        Platform.runLater(()->scanFruit.setDisable(false));
        if (error) {
            Platform.runLater(() -> alert.setContentText("Error: " + fruitName));
        } else {
            Platform.runLater(() -> alert.setContentText("Found: " + fruitName));
            currentFruit=fruitName;
        }
        Platform.runLater(() -> alert.show());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> ((Stage) alert.getDialogPane().getScene().getWindow()).close());
    }

    @Override
    public void requestCallback(int statusCode, String response) {
        Platform.runLater(() -> ((Stage) alert.getDialogPane().getScene().getWindow()).close());
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
}
