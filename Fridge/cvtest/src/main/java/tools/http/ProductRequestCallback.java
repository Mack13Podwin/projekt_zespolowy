package tools.http;

public interface ProductRequestCallback {
    void requestCallback(int statusCode, String response);
}
