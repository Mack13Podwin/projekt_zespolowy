package edu.team.programming.fridge.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationUser {
    private String login;
    private String password;
}
