package edu.team.programming.fridge.infrastructure.rest;

import edu.team.programming.fridge.domain.Barcode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BarcodeTO {
    private String name;
    private String barcode;

    public static BarcodeTO createFromBarcode(Barcode barcode){
        return BarcodeTO.builder().barcode(barcode.getBarcode()).name(barcode.getName()).build();
    }
}
