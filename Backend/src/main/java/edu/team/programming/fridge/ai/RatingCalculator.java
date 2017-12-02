package edu.team.programming.fridge.ai;

import edu.team.programming.fridge.domain.Product;
import edu.team.programming.fridge.domain.Rating;
import edu.team.programming.fridge.domain.User;
import edu.team.programming.fridge.infrastructure.db.ProductRepository;
import edu.team.programming.fridge.infrastructure.db.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class RatingCalculator implements Runnable{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void run() {
        List<User> users=usersRepository.findAll();
        for(User user:users){
            String fridgeid=user.getFridgeid();
            List<Product>products=productRepository.findByFridgeid(fridgeid);
            HashMap<String,Long> productMap=new HashMap<>();
            for(Product product:products){
                String type=product.getType();
                Long count=productMap.get(type);
                if(count!=null){
                    long l=count;
                    l++;
                    productMap.replace(type,l);
                }else{
                    long l=1;
                    productMap.put(type,l);
                }
            }
            if(productMap.values().size()>0){
                long max= Collections.max(productMap.values());
                for(String type:productMap.keySet()){
                    double rating=(double)productMap.get(type)/max;
                    Rating r=Rating.builder().fridgeid(fridgeid).type(type).rating(rating).build();
                    System.out.println(r.getType()+" "+r.getRating());
                }
            }
        }
    }
}
