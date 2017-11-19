package edu.team.programming.fridge.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@Document(collection = "barcodes")
public class Barcode {
    private String id;
    private String name;
    private String type;
    private String barcode;
}
