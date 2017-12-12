package edu.team.programming.fridge.rest.camera;

import edu.team.programming.fridge.domain.Barcode;
import edu.team.programming.fridge.domain.Product;
import edu.team.programming.fridge.infrastructure.db.BarcodeRepository;
import edu.team.programming.fridge.infrastructure.db.ProductRepository;
import edu.team.programming.fridge.infrastructure.rest.BarcodeTO;
import edu.team.programming.fridge.infrastructure.rest.CameraProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value="/camera")
public class CameraController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BarcodeRepository barcodeRepository;

    @RequestMapping(value="/product", method= RequestMethod.PUT)
    public BarcodeTO putProduct(@RequestBody CameraProduct cp){
        Barcode b=barcodeRepository.findByBarcode(cp.getBarCode());
        if(b!=null){
            Product p=Product.builder().name(b.getName()).type(b.getType()).fridgeid("00000000").barcode(cp.getBarCode()).addingdate(cp.getDate()).build();
            productRepository.save(p);
            return BarcodeTO.createFromBarcode(b);
        }else{
            ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            return null;
        }
    }

    @RequestMapping(value = "/product", method=RequestMethod.DELETE)
    public void removeProduct(@RequestBody CameraProduct cp){
        List<Product> products=productRepository.findByBarcode(cp.getBarCode());
        if(!products.isEmpty()){
            products.sort(Comparator.comparing(Product::getAddingdate));
            Product product=products.get(0);
            product.setRemovingdate(cp.getDate());
        }else{
            ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @RequestMapping(value="/product", method=RequestMethod.PATCH)
    public void openProduct(@RequestBody CameraProduct cp){
        List<Product> products=productRepository.findByBarcode(cp.getBarCode());
        if(!products.isEmpty()){
            Product p=products.stream().filter(product -> product.getOpeningdate()==null).findAny().get();
            if(p!=null){
                p.setOpeningdate(cp.getDate());
                productRepository.save(p);
            }else{
                ResponseEntity.status(HttpStatus.CONFLICT).body("Closed product not found!");
            }
        }else{
            ResponseEntity.status(HttpStatus.CONFLICT).body("Product not found!");
        }
    }

}
