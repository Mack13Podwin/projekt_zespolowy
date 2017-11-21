package edu.team.programming.fridge.domain;

import lombok.AllArgsConstructor;
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
    private String fridge_id;
    private String barcode;
    private Date adding_date;
    private Date opening_date;
    @Setter
    private Date removing_date;
    private Date expiration_date;
}
