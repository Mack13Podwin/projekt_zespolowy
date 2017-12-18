package edu.team.programming.fridge.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Document(collection="users")
public class User {
    private String id;
    private String name;
    private String password;
    private String fridgeid;
    private String country;
    private boolean firstLogin;
}
