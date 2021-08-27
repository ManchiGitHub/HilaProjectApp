package com.example.parkinson.features.metrics.add_new_medicine.single_metric;

public class SingleMetric {

    private String value;
    private long timeStamp;

    public SingleMetric(String value, long timeStamp) {
        this.value = value;
        this.timeStamp = timeStamp;
    }

    public SingleMetric() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
