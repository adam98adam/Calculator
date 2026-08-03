package com.example.test.control;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double add(double val1, double val2) {
        return val1 + val2;
    }

    public double div(double val1, double val2) {
        if (val2 == 0.0) {
            throw new IllegalArgumentException("Divisor cannot be zero.");
        }
        return val1 / val2;
    }
}
