package com.example.backend.rest;

import lombok.Data;

@Data
public class CalculatorInput {
    private String typeOfCustomer;
    private boolean fxMismatch;
    private Double debitBalance;
    private Double creditBalance;
    private Double limit;
    private Country country;
    private Rating rating;
    private Double expectedIncome;
    private Double hurdleReturn;
    private Double ingGuarantee;
}
