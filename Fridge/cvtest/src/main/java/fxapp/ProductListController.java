package fxapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import tools.http.ProductListItem;
import tools.http.ProductListRequest;
import tools.http.ProductListRequestCallback;

import java.time.LocalDate;

public class ProductListController implements IScreen, ProductListRequestCallback {
    @FXML
    public TableColumn<FXProductListItem, String> itemColumn;
    @FXML
    public TableColumn<FXProductListItem, LocalDate> addingDateColumn;
    @FXML
    public TableColumn<FXProductListItem, LocalDate> openingDateColumn;
    @FXML
    public TableColumn<FXProductListItem, LocalDate> expirationDateColumn;
    @FXML
    public TableView<FXProductListItem> productListView;
    private ScreenSwitcher screenSwitcher;

    private ObservableList<FXProductListItem> productList = FXCollections.observableArrayList();
    @FXML
    AnchorPane content;

    @Override
    public void onScreenExit() {

    }

    @Override
    public void onScreenShow() {
        ProductListRequest productListRequest = new ProductListRequest(this);
        productListRequest.start();
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
        screenSwitcher.switchToScreen(ScreenSwitcher.Screens.ACTION_CHOOSER);
    }

    public void initialize() {
        productListView.setItems(productList);
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().name);
        addingDateColumn.setCellValueFactory(cellData -> cellData.getValue().addingdate);
        openingDateColumn.setCellValueFactory(cellData->cellData.getValue().openingdate);
        expirationDateColumn.setCellValueFactory(cellData->cellData.getValue().expirationdate);
    }

    @Override
    public void listRequestCallback(int responseCode, String response) {
        System.out.println("productlistrequestcallback called");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
        ProductListItem[] productListItems = gson.fromJson(response, ProductListItem[].class);
        Platform.runLater(() -> {
            productList.clear();
            for (ProductListItem productListItem : productListItems) {
                productList.add(new FXProductListItem(productListItem));
            }
        });
        System.out.println(productListItems[0].addingdate);
        //Platform.runLater(() -> productList.add(new FXProductListItem(productListItems[0])));
    }
}
