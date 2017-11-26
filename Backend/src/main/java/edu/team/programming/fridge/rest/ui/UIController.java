package edu.team.programming.fridge.rest.ui;

import edu.team.programming.fridge.domain.Product;
import edu.team.programming.fridge.infrastructure.db.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/ui")
public class UIController {
    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/inside", method = RequestMethod.GET)
    public List<Product> getProductsInFridge(@RequestBody String fridgeId){
        return productRepository.findByFridgeidAndRemovingdateIsNull(fridgeId);
    }

    @RequestMapping(value = "/shoppinglist", method = RequestMethod.GET)
    public List<Product> getShoppingList(@RequestBody String fridgeId){
        return productRepository.findByRemovingdateBeforeAndFridgeid(new Date(),fridgeId);
    }

    @RequestMapping(value = "/expired", method=RequestMethod.GET)
    public List<Product> getExpired(@RequestBody String fridgeId){
        return productRepository.findByFridgeidAndExpirationdateBeforeAndRemovingdateNotNull(fridgeId,new Date());
    }
}
