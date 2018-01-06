package edu.team.programming.fridge.ai;

import edu.team.programming.fridge.domain.Rating;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Recommender {

    private Map<String,Map<String,Double>> diff=new HashMap<>();
    private Map<String,Map<String,Integer>> freq=new HashMap<>();
    @Getter
    private Map<String,Map<String,Double>> outputData=new HashMap<>();
    private List<String> inputData;

    public void calculateRecommendations(List<Rating> ratings){
        inputData=new ArrayList<>();
        HashMap<String,HashMap<String,Double>> data=new HashMap<>();
        for (Rating rating:ratings){
            if(!data.containsKey(rating.getFridgeid())){
                data.put(rating.getFridgeid(),new HashMap<>());
            }
            if(!inputData.contains(rating.getType())){
                inputData.add(rating.getType());
            }
            HashMap<String,Double> r=data.get(rating.getFridgeid());
            r.put(rating.getType(),rating.getRating());
            data.replace(rating.getFridgeid(),r);
        }
        buildDifferencesMatrix(data);
        predict(data);
    }

    public List<Rating>getRatings(String fridgeid){
        List<Rating> ratings=new ArrayList<>();
        for (String type: outputData.get(fridgeid).keySet()){
            Rating rating=Rating.builder()
                    .fridgeid(fridgeid)
                    .type(type)
                    .rating(outputData.get(fridgeid).get(type))
                    .build();
            ratings.add(rating);
        }
        return ratings;
    }

    private void buildDifferencesMatrix(HashMap<String,HashMap<String,Double>> data){
        for (HashMap<String, Double> user : data.values()) {
            for (Map.Entry<String, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<>());
                    freq.put(e.getKey(), new HashMap<>());
                }
                for (Map.Entry<String, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey());
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey());
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (String j : diff.keySet()) {
            for (String i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i);
                int count = freq.get(j).get(i);
                diff.get(j).put(i, oldValue / count);
            }
        }
    }

    private void predict(HashMap<String,HashMap<String,Double>> data){
        HashMap<String, Double> uPred = new HashMap<>();
        HashMap<String, Integer> uFreq = new HashMap<>();
        for (String j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Map.Entry<String, HashMap<String, Double>> e : data.entrySet()) {
            for (String j : e.getValue().keySet()) {
                for (String k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j) + e.getValue().get(j);
                        double finalValue = predictedValue * freq.get(k).get(j);
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j));
                    } catch (NullPointerException ignored) {

                    }
                }
            }
            HashMap<String, Double> clean = new HashMap<>();
            for (String j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j) / uFreq.get(j));
                }
            }
            for (String j : inputData) {
                clean.put(j, e.getValue().getOrDefault(j, -1.0));
            }
            outputData.put(e.getKey(), clean);
        }
    }
}
