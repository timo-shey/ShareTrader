package com.example.SharesBrokeringSystem.dto;

import java.util.Map;

public class FinnhubMetricResponse {
    private Map<String, Double> metric;

    public Map<String, Double> getMetric() {
        return metric;
    }

    public void setMetric(Map<String, Double> metric) {
        this.metric = metric;
    }
}
