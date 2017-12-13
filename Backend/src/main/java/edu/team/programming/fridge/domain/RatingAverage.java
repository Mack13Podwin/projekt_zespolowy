package edu.team.programming.fridge.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RatingAverage {
    private String fridgeid;
    private double average;
}
