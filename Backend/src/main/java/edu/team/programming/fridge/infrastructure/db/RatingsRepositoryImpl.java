package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.Rating;
import edu.team.programming.fridge.domain.RatingAverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;


public class RatingsRepositoryImpl implements RatingsRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public RatingsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private MatchOperation getMatchOperation(String fridgeid){
        Criteria userCriteria=where("fridgeid").is(fridgeid);
        return match(userCriteria);
    }

    private GroupOperation getGroupOperation(){
        return group("fridgeid")
                .last("fridgeid").as("fridgeid")
                .avg("rating").as("average");
    }

    private ProjectionOperation getProjectionOperation(){
        return project("average").and("fridgeid").previousOperation();
    }

    @Override
    public List<RatingAverage> aggregate(String fridgeid) {
        MatchOperation matchOperation=getMatchOperation(fridgeid);
        GroupOperation groupOperation=getGroupOperation();
        ProjectionOperation projectionOperation=getProjectionOperation();
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                matchOperation,
                groupOperation,
                projectionOperation
        ), Rating.class,RatingAverage.class).getMappedResults();
    }
}
