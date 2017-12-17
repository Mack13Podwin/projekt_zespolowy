package tools.http;

public enum HTTPService {

        INSTANCE;

        public HTTPService getInstance(){
            return INSTANCE;
        }
        private final String PATH_PREFIX="";
        public final String DOMAIN_NAME = "http://localhost:8090"+PATH_PREFIX; //adres serwera
        public final String PRODUCT_LIST_PATH = "/camera/products/";
        public final String PRODUCT_OPERATION_PATH="/camera/product/";
        public final String FridgeId="00000005";
        private HTTPService (){

        }


    public static void fetchProductList() {

    }
}
