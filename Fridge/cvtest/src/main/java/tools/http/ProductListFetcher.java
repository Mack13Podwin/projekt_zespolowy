package tools.http;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import tools.http.HTTPService;

import java.io.IOException;


public class ProductListFetcher extends Thread {
    private String FridgeId;
    private HTTPService service;
    public ProductListFetcher(String FridgeId){
        this.FridgeId=FridgeId;
    }
    @Override
    public void run(){
        String getUrl=service.DOMAIN_NAME+service.PRODUCT_LIST_PATH+FridgeId;
        Gson gson=new Gson();
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpGet httpGet=new HttpGet(getUrl);
        try {
            HttpResponse response=httpClient.execute(httpGet);
            System.out.println(response.getEntity().getContent().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
