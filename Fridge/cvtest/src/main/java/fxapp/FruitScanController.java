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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.http.entity.mime.MultipartEntityBuilder;
public class FruitScanController implements IScreen{
    ArrayList<String> fruits=new ArrayList<>(Arrays.asList("banana","mandarin","apple", "grapefruit", "lemon", "orange"));
    @FXML
    private AnchorPane content;
    @FXML
    private  ChoiceBox scanFruit;
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
    private Camera cam= CameraFactory.getInstance();
    private ScheduledExecutorService timer;
    private ScreenSwitcher screenSwitcher;

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
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.SCANNING_SCREEN);
    }

    public void nextClicked(MouseEvent mouseEvent) {
    }

    public void initialize(){
        LocalDate date=LocalDate.now().plusDays(1);
        datePicker.setValue(date);
    }

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


    public void scanClick(MouseEvent mouseEvent) {
        Mat frame=new Mat();
        Mat dst=new Mat();
        cam.captureFrame(frame);
        BufferedImage buf=CVUtils.MatToBufferedImage(frame);
        frame.release();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(buf, "png", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Header> defaultHeaders = Arrays.asList(
                new BasicHeader("Authorization", ""));
        HttpClient httpClient = HttpClientBuilder.create().setDefaultHeaders(defaultHeaders).build();
        HttpPost httpPost=new HttpPost("https://api.imagga.com/v1/content");
        MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
        multiPartEntityBuilder.addBinaryBody("Picture", stream.toByteArray(), ContentType.create("image/png"), "image.png");
        httpPost.setEntity(multiPartEntityBuilder.build());
        String id="";
        try {
            System.out.println("sending post request");
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println((response.getStatusLine().getStatusCode()));
            String responseString = null;
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println("response: " + responseString);
            Gson gson=new Gson();
            JsonObject jsonObject=gson.fromJson(responseString, JsonObject.class);
            JsonArray jsonArray=jsonObject.getAsJsonArray("uploaded");
            JsonElement jsonElement=jsonArray.get(0);
            id=((JsonObject)jsonElement).get("id").getAsString();
            System.out.println(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpGet httpGet=new HttpGet("https://api.imagga.com/v1/tagging?content="+id);
        try {

            System.out.println("sending get request");
            HttpResponse response = httpClient.execute(httpGet);
            System.out.println((response.getStatusLine().getStatusCode()));
            String responseString = null;
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println("response: " + responseString);
            Gson gson=new Gson();
            JsonObject jsonObject=gson.fromJson(responseString, JsonObject.class);
            JsonArray jsonArray=jsonObject.getAsJsonArray("results");
            JsonElement jsonElement=jsonArray.get(0);
            JsonArray tags=((JsonObject)jsonElement).getAsJsonArray("tags");
            for(JsonElement tag:tags){
                String tagname=((JsonObject)tag).get("tag").getAsString();
                if(fruits.indexOf(tagname)!=-1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.getButtonTypes().clear();
                    alert.setContentText("Found: "+tagname);
                    alert.show();
                    Thread.sleep(3000);
                    Platform.runLater(()->((Stage) alert.getDialogPane().getScene().getWindow()).close());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
