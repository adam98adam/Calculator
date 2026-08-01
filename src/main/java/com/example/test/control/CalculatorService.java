package com.example.test.control;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double add(double val1, double val2) {
        return val1 + val2;
    }
}
