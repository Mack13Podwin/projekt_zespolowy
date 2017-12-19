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
    @Setter
    private String name;
    @Setter
    private String password;
    private String fridgeid;
    private String country;
    @Setter
    private boolean firstLogin;
    @Setter
    private String email;
}
