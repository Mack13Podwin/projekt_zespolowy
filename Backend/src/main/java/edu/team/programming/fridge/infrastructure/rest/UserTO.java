package edu.team.programming.fridge.infrastructure.rest;

import edu.team.programming.fridge.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Base64;

@Getter
@Builder
public class UserTO {
    private String id;
    private String fridgeid;
    private String country;
    private boolean firstLogin;
    private String token;

    public static UserTO createFromUser(User user, String t){
        return UserTO.builder().country(user.getCountry()).fridgeid(user.getFridgeid()).id(user.getId()).firstLogin(user.isFirstLogin()).token(t).build();
    }
}
