package tools.http;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import tools.http.HTTPService;

import java.io.IOException;


public class ProductListRequest extends Thread {

    private HTTPService service=HTTPService.INSTANCE;
    private ProductListRequestCallback callback;
    public ProductListRequest(ProductListRequestCallback callback){
        this.callback=callback;
    }
    @Override
    public void run(){
        String getUrl=service.DOMAIN_NAME+service.PRODUCT_LIST_PATH;
        Gson gson=new Gson();
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpGet httpGet=new HttpGet(getUrl);
        httpGet.setHeader("authorization", service.FridgeId);
        try {
            HttpResponse response=httpClient.execute(httpGet);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println("productlistrequestcallback response: "+responseString);
            callback.listRequestCallback(response.getStatusLine().getStatusCode(),responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
