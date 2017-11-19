package edu.team.programming.fridge.rest.camera;

import edu.team.programming.fridge.domain.Product;
import edu.team.programming.fridge.infrastructure.db.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/camera")
public class CameraController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value="/product", method= RequestMethod.PUT)
    public Product putProduct(@RequestBody String barCode){
        return new Product(barCode, "mleko","mleko","00000000",null,null,null,null);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Product> getAllProducts(){return productRepository.findAll();}

}
