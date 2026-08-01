package com.example.test.boundary.dto;

public class AddResponse {
    private double value;

    public AddResponse(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
