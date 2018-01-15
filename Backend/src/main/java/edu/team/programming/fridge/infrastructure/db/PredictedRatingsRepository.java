package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.PredictedRating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PredictedRatingsRepository extends MongoRepository<PredictedRating,String> {
    List<PredictedRating> findByFridgeidAndType(String fridgeid, String type);

    List<PredictedRating> findByFridgeidOrderByRatingDesc(String fridgeId);
}
