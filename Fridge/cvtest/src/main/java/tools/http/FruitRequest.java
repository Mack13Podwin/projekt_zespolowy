package tools.http;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fxapp.FruitScanController;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FruitRequest extends Thread{
    BufferedImage fruitPhoto;
    FruitRequestCallback callback;
   public FruitRequest(BufferedImage img,FruitRequestCallback callback){
       fruitPhoto=img;
       this.callback=callback;
   }
   @Override
   public void run(){
       ByteArrayOutputStream stream = new ByteArrayOutputStream();
       try {
           ImageIO.write(fruitPhoto, "png", stream);
       } catch (IOException e) {
           e.printStackTrace();
       }
       List<Header> defaultHeaders = Arrays.asList(
               new BasicHeader("Authorization", "Basic YWNjX2Y4Njc2NzhmOWM2ODUxMTo0ZGJlMzY4MzU0Y2MzYTg3OGRmZjdjOTc3MzM3M2FlMw=="));
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
           callback.fruitRequestCallback(true, "IO Error:"+e.getMessage());
           e.printStackTrace();
           return;
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
               if(FruitScanController.fruits.indexOf(tagname)!=-1){
                  callback.fruitRequestCallback(false,tagname);
                   break;
               }
           }
       } catch (IOException e) {
           callback.fruitRequestCallback(true, "IO Error:"+e.getMessage());
           e.printStackTrace();
       }

   }
}
