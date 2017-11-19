package edu.team.programming.fridge.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    private String id;
    private String name;
    private String type;
    private String fridge_id;
    private Date adding_date;
    private Date opening_date;
    private Date removing_date;
    private Date expiration_date;
}
