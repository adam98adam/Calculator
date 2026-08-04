package com.example.test.boundary.rest;

import com.example.test.boundary.dto.Request;
import com.example.test.boundary.dto.Response;
import com.example.test.control.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {
    final private CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping(value = "/add")
    private Response add(@RequestBody Request request) {
        double value = calculatorService.add(request.val1(), request.val2());
        return new Response(value);
    }

    @GetMapping(value = "/div")
    private Response div(@ModelAttribute Request request) {
        double value = calculatorService.divide(request.val1(), request.val2());
        return new Response(value);
    }
}
