package edu.team.programming.fridge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Builder
@Document(collection = "products")
public class Product {
    private String id;
    private String name;
    private String type;
    private String fridgeid;
    private String barcode;
    private Date addingdate;
    @Setter
    private Date openingdate;
    @Setter
    private Date removingdate;
    private Date expirationdate;
}
