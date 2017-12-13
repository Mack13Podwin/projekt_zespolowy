package edu.team.programming.fridge.infrastructure.db;

import edu.team.programming.fridge.domain.RatingAverage;

import java.util.List;

public interface RatingsRepositoryCustom {
    List<RatingAverage> aggregate(String fridgeid);
}
