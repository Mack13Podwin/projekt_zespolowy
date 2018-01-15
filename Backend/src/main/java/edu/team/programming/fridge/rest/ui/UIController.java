package edu.team.programming.fridge.rest.ui;

import edu.team.programming.fridge.ai.RatingCalculator;
import edu.team.programming.fridge.ai.Recommender;
import edu.team.programming.fridge.domain.PredictedRating;
import edu.team.programming.fridge.domain.Product;
import edu.team.programming.fridge.domain.Rating;
import edu.team.programming.fridge.domain.RatingAverage;
import edu.team.programming.fridge.exception.RecipeNotFoundException;
import edu.team.programming.fridge.infrastructure.db.PredictedRatingsRepository;
import edu.team.programming.fridge.infrastructure.db.ProductRepository;
import edu.team.programming.fridge.infrastructure.db.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/ui")
public class UIController {

    @Value("${recipes.api.key}")
    private String apiKey;

    @Value("${recipes.app.id}")
    private String appId;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RatingCalculator ratingCalculator;

    @Autowired
    private Recommender recommender;

    @Autowired
    private PredictedRatingsRepository predictedRatingsRepository;

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
        List<Product> products=productRepository.findAll();
        recommender.calculateRecommendations(products);
        return "OK\n";
    }
    @RequestMapping(value = "/shoppinglist/{fridgeId}", method = RequestMethod.GET)
    public List<PredictedRating> getShoppingList(@PathVariable String fridgeId){
        List<PredictedRating>ratings= predictedRatingsRepository.findByFridgeidOrderByRatingDesc(fridgeId);
        List<PredictedRating> result= new ArrayList<>();
        for (PredictedRating rating:ratings){
            if(productRepository.findByFridgeidAndTypeAndRemovingdateIsNull(fridgeId,rating.getType()).size()==0){
                if(!result.contains(rating)&&rating.getRating()>=0.0) {
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
    @RequestMapping(value = "/recipe/{fridgeId}", method=RequestMethod.GET)
    public String getRecipe(@PathVariable String fridgeId) throws RecipeNotFoundException {
        List<Product> products = productRepository.findByFridgeid(fridgeId);
        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> outputLines = new ArrayList<>();
        for(Product product : products)
            types.add(product.getType());
        Random rand = new Random();
        String ing = types.get(rand.nextInt(types.size()));
        try {
            URL url = new URL("https://api.edamam.com/search?q=" + ing + "&app_id="+appId+"&app_key="+apiKey+"&from=0&to=1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            boolean saveLine = false;
            outputLines.add("{");
            while ((output = br.readLine()) != null) {
                if (output.contains("hits"))
                    saveLine=true;
                if (output.contains("ingredients"))
                    saveLine=false;
                if(output.contains("ingredientLines"))
                {
                    int outLength = output.length();
                    output = output.substring(0,outLength-1);
                }
                if(saveLine)
                    outputLines.add(output);
            }
            outputLines.add("}");
            outputLines.add("}");
            outputLines.add("]");
            outputLines.add("}");

            StringBuilder builder = new StringBuilder();

            for (String string : outputLines) {
                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append(string);
            }

            conn.disconnect();

            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }catch(RuntimeException ex){
            ex.printStackTrace();
            throw new RecipeNotFoundException();
        }
        return "";
    }
}
