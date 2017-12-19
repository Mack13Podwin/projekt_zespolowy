package tools.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ProductRequest extends Thread {

    private String FridgeId;
    private HTTPService service = HTTPService.INSTANCE;
    private ProductRequestCallback callback;
    private Product product;
    private RequestTypes requestType;
    public enum RequestTypes{
        NEW, OPEN, DELETE
    }
    public ProductRequest(Product product, ProductRequestCallback callback, RequestTypes requestType) {
        this.callback=callback;
        this.product = product;
        this.requestType=requestType;
    }
    private HttpRequestBase prepareRequest(String Url,Gson gson) throws UnsupportedEncodingException {
        switch (requestType){
            case NEW:
                return createPutRequest(Url,gson);
            case DELETE:
                return createDeleteRequest(Url,gson);
            case OPEN:
                return createOpenRequest(Url, gson);

        }
        return null;
    }
    private HttpPut createPutRequest(String url, Gson gson) throws UnsupportedEncodingException {
        HttpPut httpPut = new HttpPut(url);
        StringEntity putEntity=new StringEntity(gson.toJson(product));
        System.out.println(gson.toJson(product));
        httpPut.setEntity(putEntity);
        httpPut.setHeader("authorization", service.FridgeId);
        httpPut.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        return httpPut;
    }
    private HttpPatch createOpenRequest(String url, Gson gson) throws UnsupportedEncodingException {
        HttpPatch httpPatch = new HttpPatch(url);
        StringEntity putEntity=new StringEntity(gson.toJson(product));
        System.out.println(gson.toJson(product));
        httpPatch.setEntity(putEntity);
        httpPatch.setHeader("authorization", service.FridgeId);
        httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        return httpPatch;
    }
    private HttpPatch createDeleteRequest(String url, Gson gson) throws UnsupportedEncodingException {
        url+="delete";
        HttpPatch httpPatch = new HttpPatch(url);
        StringEntity putEntity=new StringEntity(gson.toJson(product));
        System.out.println(gson.toJson(product));
        httpPatch.setEntity(putEntity);
        httpPatch.setHeader("authorization", service.FridgeId);
        httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        return httpPatch;
    }
    @Override
    public void run() {
        String getUrl = service.DOMAIN_NAME + service.PRODUCT_OPERATION_PATH;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
        HttpClient httpClient = HttpClientBuilder.create().build();



        try {

            HttpResponse response = httpClient.execute(prepareRequest(getUrl,gson));
            System.out.println((response.getStatusLine().getStatusCode()));
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println("response: " + responseString);
            callback.requestCallback(response.getStatusLine().getStatusCode(),responseString);
            //TODO finish
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
