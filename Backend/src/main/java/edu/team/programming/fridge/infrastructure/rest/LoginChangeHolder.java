package edu.team.programming.fridge.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginChangeHolder {
    private String oldlogin, newlogin;
}
