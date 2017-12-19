package fxapp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import tools.http.ProductListItem;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class FXProductListItem {
    public StringProperty id=new SimpleStringProperty();
    public StringProperty name=new SimpleStringProperty();
    public StringProperty type=new SimpleStringProperty();
    public StringProperty fridgeid=new SimpleStringProperty();
    public StringProperty barcode=new SimpleStringProperty();
    public ObjectProperty<LocalDate> addingdate=new SimpleObjectProperty<>();
    public ObjectProperty<LocalDate> openingdate=new SimpleObjectProperty<>();
    public ObjectProperty<LocalDate> removingdate=new SimpleObjectProperty<>();
    public ObjectProperty<LocalDate> expirationdate=new SimpleObjectProperty<>();
    public FXProductListItem(ProductListItem productListItem){
        this.id.setValue(productListItem.id);
        this.name.setValue(productListItem.name);
        this.type.setValue(productListItem.type);
        this.fridgeid.setValue(productListItem.fridgeid);
        this.barcode.setValue(productListItem.barcode);
        expirationdate.setValue(dateToLocalDate(productListItem.expirationdate));
        removingdate.setValue(dateToLocalDate(productListItem.removingdate));
        addingdate.setValue(dateToLocalDate(productListItem.addingdate));
        openingdate.setValue(dateToLocalDate(productListItem.openingdate));

    }
    private LocalDate  dateToLocalDate(Date input){
        if (input==null)
            return null;
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
