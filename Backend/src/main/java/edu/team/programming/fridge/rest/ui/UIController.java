package edu.team.programming.fridge.rest.ui;

import edu.team.programming.fridge.ai.RatingCalculator;
import edu.team.programming.fridge.ai.Recommender;
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

import java.util.ArrayList;
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

    @Autowired
    private Recommender recommender;

    @RequestMapping(value = "/inside/{fridgeId}", method = RequestMethod.GET)
    public List<Product> getProductsInFridge(@PathVariable String fridgeId){
        System.out.println("Getting products from fridge "+fridgeId);
        return productRepository.findByFridgeidAndRemovingdateIsNull(fridgeId);
    }
    @RequestMapping(value = "/calculateRating", method=RequestMethod.GET)
    public String calculateRating(){
        Thread calc=new Thread(ratingCalculator);
        calc.start();
        try {
            calc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Rating> ratings=ratingsRepository.findAll();
        recommender.calculateRecommendations(ratings);
        return "OK\n";
    }
    @RequestMapping(value = "/shoppinglist/{fridgeId}", method = RequestMethod.GET)
    public List<Rating> getShoppingList(@PathVariable String fridgeId){
        RatingAverage average=ratingsRepository.aggregate(fridgeId).get(0);
        List<Rating>ratings=
                ratingsRepository.findByFridgeidAndRatingGreaterThanEqualOrderByRatingDesc(fridgeId,
                        average.getAverage());
        ratings.addAll(recommender.getRatings(fridgeId));
        List<Rating> result= new ArrayList<>();
        for (Rating rating:ratings){
            if(productRepository.findByFridgeidAndTypeAndRemovingdateIsNull(fridgeId,rating.getType()).size()==0){
                if(!result.contains(rating)&&rating.getRating()>0) {
                    result.add(rating);
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/expired/{fridgeId}", method=RequestMethod.POST)
    public List<Product> getExpired(@PathVariable String fridgeId){
        return productRepository.findByFridgeidAndExpirationdateBeforeAndRemovingdateNotNull(fridgeId,new Date());
    }
}
