package edu.team.programming.fridge.rest.camera;

import edu.team.programming.fridge.domain.Barcode;
import edu.team.programming.fridge.domain.Product;
import edu.team.programming.fridge.infrastructure.db.BarcodeRepository;
import edu.team.programming.fridge.infrastructure.db.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/camera")
public class CameraController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BarcodeRepository barcodeRepository;

    @RequestMapping(value="/product", method= RequestMethod.PUT)
    public Barcode putProduct(@RequestBody String barCode){
        Barcode b=barcodeRepository.findByBarcode(barCode);
        Product p=Product.builder().name(b.getName()).type(b.getType()).fridge_id("19403204").barcode(barCode).adding_date(new Date()).build();
        productRepository.save(p);
        return b;
    }

    @RequestMapping(value = "/product", method=RequestMethod.DELETE)
    public void removeProduct(@RequestBody String barCode){
        List<Product> products=productRepository.findByBarcode(barCode);
        if(!products.isEmpty()){
            products.sort(Comparator.comparing(Product::getAdding_date));
            Product product=products.get(0);
            product.setRemoving_date(new Date());
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

}
