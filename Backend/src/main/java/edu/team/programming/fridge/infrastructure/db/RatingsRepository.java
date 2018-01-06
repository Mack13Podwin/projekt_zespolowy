package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface RatingsRepository extends MongoRepository<Rating,String>,RatingsRepositoryCustom {
       List<Rating> findByFridgeidAndRatingGreaterThanEqualOrderByRatingDesc(String fridgeId, double rating);
       List<Rating> findAll();
}
