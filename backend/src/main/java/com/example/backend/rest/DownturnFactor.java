package com.example.backend.rest;

public enum DownturnFactor {
    CorporateClients(0.5),
    SMENL(1),
    CPF(2.3),
    FinancialInstitution(0.5);

    private final double downTurnFactorValue;

    DownturnFactor(double downTurnFactorValue) {
        this.downTurnFactorValue = downTurnFactorValue;
    }

    public double getDownTurnFactorValue() {
        return this.downTurnFactorValue;
    }
}
