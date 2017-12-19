package edu.team.programming.fridge.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Document(collection="users")
public class User implements Serializable{
    private String id;
    private String name;
    private String password;
    private String fridgeid;
    private String country;
    private boolean firstLogin;
    @Setter
    private String email;
}
