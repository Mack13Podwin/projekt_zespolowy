package edu.team.programming.fridge.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection="sessionTokens")
public class SessionToken {
    private String token;
    private String userId;
}
