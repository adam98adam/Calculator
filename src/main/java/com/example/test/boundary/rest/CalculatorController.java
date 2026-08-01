package com.example.test.boundary.rest;

import com.example.test.boundary.dto.AddRequest;
import com.example.test.control.CalculatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {
    final private CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/add")
    private double add(@RequestBody AddRequest addRequest){
        return calculatorService.add(addRequest.getVal1(), addRequest.getVal2());
    }

}
