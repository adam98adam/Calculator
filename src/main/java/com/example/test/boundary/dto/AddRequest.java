package com.example.test.boundary.dto;

public class AddRequest {
    private double val1;
    private double val2;

    public AddRequest(double val1, double val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public double getVal1() {
        return val1;
    }

    public void setVal1(double val1) {
        this.val1 = val1;
    }

    public double getVal2() {
        return val2;
    }

    public void setVal2(double val2) {
        this.val2 = val2;
    }
}
