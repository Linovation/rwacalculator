package com.example.backend.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller {
    @Autowired
    CalculatorService calculatorService;
    @PostMapping("/calculate")
    public ResponseEntity<CalculatorResult> calculateRwa(@RequestBody CalculatorInput calculatorInput) {
        log.info("get called");
        CalculatorResult calculatorResult = calculatorService.calculate(calculatorInput);
        return ResponseEntity.ok(calculatorResult);
    }
}
