package edu.team.programming.fridge.rest.ui;

import edu.team.programming.fridge.ai.RatingCalculator;
import edu.team.programming.fridge.domain.Product;
import edu.team.programming.fridge.domain.Rating;
import edu.team.programming.fridge.domain.RatingAverage;
import edu.team.programming.fridge.infrastructure.db.ProductRepository;
import edu.team.programming.fridge.infrastructure.db.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private RatingCalculator ratingCalculator;

    @Autowired
    private RatingsRepository ratingsRepository;

    @RequestMapping(value = "/inside/{fridgeId}", method = RequestMethod.GET)
    public List<Product> getProductsInFridge(@PathVariable String fridgeId){
        System.out.println("Getting products from fridge "+fridgeId);
        return productRepository.findByFridgeidAndRemovingdateIsNull(fridgeId);
    }

    @RequestMapping(value = "/shoppinglist/{fridgeId}", method = RequestMethod.GET)
    public List<Rating> getShoppingList(@PathVariable String fridgeId){
        Thread calc=new Thread(ratingCalculator);
        calc.start();
        try {
            calc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RatingAverage average=ratingsRepository.aggregate(fridgeId).get(0);
        return ratingsRepository.findByFridgeidAndRatingGreaterThanEqualOrderByRatingRatingDesc(fridgeId,average.getAverage());
    }

    @RequestMapping(value = "/expired/{fridgeId}", method=RequestMethod.POST)
    public List<Product> getExpired(@PathVariable String fridgeId){
        return productRepository.findByFridgeidAndExpirationdateBeforeAndRemovingdateNotNull(fridgeId,new Date());
    }
}
