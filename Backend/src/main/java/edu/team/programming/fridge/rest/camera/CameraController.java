package edu.team.programming.fridge.rest.camera;

import edu.team.programming.fridge.domain.Product;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/camera")
public class CameraController {

    @RequestMapping(value="/product", method= RequestMethod.PUT)
    public Product putProduct(@RequestBody String barCode){
        return new Product(barCode, "mleko","mleko","00000000",null,null,null,null);
    }

}
