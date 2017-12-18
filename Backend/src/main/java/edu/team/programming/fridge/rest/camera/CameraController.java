package edu.team.programming.fridge.rest.camera;

import edu.team.programming.fridge.domain.Barcode;
import edu.team.programming.fridge.domain.Product;
import edu.team.programming.fridge.exception.ConflictException;
import edu.team.programming.fridge.infrastructure.db.BarcodeRepository;
import edu.team.programming.fridge.infrastructure.db.ProductRepository;
import edu.team.programming.fridge.infrastructure.rest.BarcodeTO;
import edu.team.programming.fridge.infrastructure.rest.CameraProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value="/camera")
public class CameraController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BarcodeRepository barcodeRepository;

    @RequestMapping(value="/product", method= RequestMethod.PUT)
    public BarcodeTO putProduct(@RequestBody CameraProduct cp, @RequestHeader(required = true, name = "authorization") String authorization) throws ConflictException{
        Barcode b=barcodeRepository.findByBarcode(cp.getBarcode());
        if(b!=null){
            Product p=Product.builder().name(b.getName()).type(b.getType()).fridgeid(authorization).barcode(cp.getBarcode()).addingdate(cp.getDate()).build();
            productRepository.save(p);
            return BarcodeTO.createFromBarcode(b);
        }else{
            throw new ConflictException();
        }
    }

    @RequestMapping(value = "/product/delete", method=RequestMethod.PATCH)
    public void removeProduct(@RequestBody CameraProduct cp, @RequestHeader(required = true, name="authorization") String authorization) throws ConflictException {
        List<Product> products=productRepository.findByBarcodeAndFridgeid(cp.getBarcode(), authorization);
        if(!products.isEmpty()){
            products.sort(Comparator.comparing(Product::getAddingdate));
            Product product=products.get(0);
            product.setRemovingdate(cp.getDate());
            productRepository.save(product);
        }else{
            throw new ConflictException();
        }
    }

    @RequestMapping(value="/product", method=RequestMethod.PATCH)
    public void openProduct(@RequestBody CameraProduct cp, @RequestHeader(required = true, name="authorization") String authorization) throws ConflictException {
        List<Product> products=productRepository.findByBarcodeAndFridgeid(cp.getBarcode(), authorization);
        if(!products.isEmpty()){
            try{
                Product p=products.stream().filter(product -> product.getOpeningdate()==null).findAny().get();
                p.setOpeningdate(cp.getDate());
                productRepository.save(p);
            }catch(NoSuchElementException ex){
                throw new ConflictException();
            }
        }else{
            throw new ConflictException();
        }
    }

}
