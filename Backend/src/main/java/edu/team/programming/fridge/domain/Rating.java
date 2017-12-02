package edu.team.programming.fridge.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "ratings")
public class Rating {
    private String id;
    private String fridgeid;
    private String type;
    private double rating;
}
