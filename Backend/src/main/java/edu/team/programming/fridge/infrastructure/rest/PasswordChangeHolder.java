package edu.team.programming.fridge.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public class PasswordChangeHolder {
    private String oldpassword, newpassword;
}
