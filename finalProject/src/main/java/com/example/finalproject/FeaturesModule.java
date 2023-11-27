package com.example.finalproject;

public class FeaturesModule {

    int id;
    String featureName;

    public FeaturesModule(int id, String featureName) {
        this.id = id;
        this.featureName = featureName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    @Override
    public String toString() {
        return featureName;
    }
}
