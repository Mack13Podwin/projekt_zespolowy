package edu.team.programming.fridge.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@EqualsAndHashCode
@Document(collection = "ratings")
public class Rating {
    private transient String id;
    private String fridgeid;
    private String type;
    private transient double rating;
}
