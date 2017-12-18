package edu.team.programming.fridge.infrastructure.rest;

import edu.team.programming.fridge.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTO {
    private String id;
    private String fridgeid;
    private String country;
    private boolean firstLogin;

    public static UserTO createFromUser(User user){
        return UserTO.builder().country(user.getCountry()).fridgeid(user.getFridgeid()).id(user.getId()).firstLogin(user.isFirstLogin()).build();
    }
}
