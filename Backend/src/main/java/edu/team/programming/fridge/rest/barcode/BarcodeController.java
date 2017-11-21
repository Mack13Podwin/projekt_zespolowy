package edu.team.programming.fridge.rest.barcode;

import edu.team.programming.fridge.domain.Barcode;
import edu.team.programming.fridge.infrastructure.db.BarcodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/barcodes")
public class BarcodeController {

    @Autowired
    private BarcodeRepository barcodeRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Barcode> getAllBarcodes(){
        return barcodeRepository.findAll();
    }
}
