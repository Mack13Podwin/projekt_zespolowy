package edu.team.programming.fridge.infrastructure.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CameraProduct {
    private Date date;
    private String barcode;
}
