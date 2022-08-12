package com.example.backend.rest;

import lombok.Data;

@Data
public class CalculatorResult {
    private Double ead;
    private Double lgd;
    private Double npvRecSecCash;
    private Double npvRecUnSecCash;
    private Double rwaCash;
    private Double npvRecSecCashGuar;
    private Double npvRecUnSecCashGuar;
    private Double lgdCashGuar;
    private Double rwaCashGuar;
    private Double cost;
    private boolean goodInvestment;
}
