package com.example.backend.rest;

public class ClientTypeStaticData {

    private static ClientType clientType;

    public static ClientType getClientType() {
        return clientType;
    }

    public void setClientType(String clientTypeInput) {
        switch (clientTypeInput) {
            case "SMENL":
                clientType = ClientType.SMENL;
                break;
            case "CPF":
                clientType = ClientType.CPF;
                break;
            case "Financial institution":
                clientType = ClientType.FinancialInstitution;
                break;
            case "Corporate clients":
                clientType = ClientType.CorporateClients;
        }
    }


    public enum ClientType {
        CorporateClients,
        SMENL,
        CPF,
        FinancialInstitution;
    }

    public static double getkFactorU() {
        switch (getClientType().toString()) {
            case "SMENL":
                return 0.25;
            case "CPF":
                return 1;
            default:
                return 0.1;
        }
    }

    public static double getFxMismatchHairCut() {
        switch (getClientType().toString()) {
            case "SMENL":
                return 0.13;
            case "CPF":
                return 0.0808;
            case "FinancialInstitution":
                return 0.0810;
            default:
                return 0.08;
        }
    }

    public static double getNoFxMismatchHairCut() {
        switch (getClientType().toString()) {
            case "SMENL":
                return 0.05;
            case "CPF":
                return 0.0008;
            case "FinancialInstitution":
                return 0.0001;
            default:
                return 0;
        }
    }

    public static double geteFactorOs() {
        if ("SMENL".equals(getClientType().toString())) {
            return 0.1;
        }
        return 0;
    }

    public static double getCost() {
        switch (getClientType().toString()) {
            case "SMENL":
                return 0.01;
            case "CPF":
                return 0.023;
            default:
                return 0.005;
        }
    }

    public static double getCostCap() {
        switch (getClientType().toString()) {
            case "SMENL":
                return 1000000000;
            case "CPF":
                return 600000;
            default:
                return 100000;
        }
    }

    public static double getDownturnFactor() {
        switch (getClientType().toString()) {
            case "CPF":
            case "FinancialInstitution":
                return 1.25;
            case "SMENL":
            default:
                return 1;
        }
    }

    public static double getDiscountFactorGen() {
        switch (getClientType().toString()) {
            case "CorporateClients":
                return 0.9294;
            case "SMENL":
                return 0.9524;
            default:
                return 1;
        }
    }

    public static double getMaturity() {
        return 1;
    }

    public static double getDisFactorCash() {
        switch (getClientType().toString()) {
            case "CorporateClients":
            case "SMENL":
                return 0.999;
            default:
                return 1;
        }
    }

    public static double getNoLossRate(String countryType) {
        switch (getClientType().toString()) {
            case "SMENL":
                return 0.3;
            case "CPF":
                return 0.5;
            case "FinancialInstitution":
                return 0;
            case "CorporateClients":
                switch (countryType) {
                    case "A":
                        return 0.43;
                    case "B":
                        return 0.33;
                    case "C":
                        return 0.28;
                    case "D":
                        return 0.2;
                    case "E":
                        return 0;
                }
        }
        return 0;
    }

    public static double getHFactorInternalGuarantee(String countryType) {
        switch (getClientType().toString()) {
            case "SMENL":
            case "FinancialInstitution":
            case "CPF":
                return 1;
            case "CorporateClients":
                switch (countryType) {
                    case "A":
                    case "B":
                        return 0;
                    case "C":
                        return 0.34;
                    case "D":
                        return 0.6;
                    case "E":
                        return 0.7;
                }
        }
        return 0;
    }

    public static double gethFactorUnsecured(String countryType) {
        switch (getClientType().toString()) {
            case "SMENL":
                return 0.85;
            case "FinancialInstitution":
                switch (countryType) {
                    case "A":
                    case "B":
                        return 0.3;
                    case "C":
                        return 0.4;
                    case "D":
                        return 0.5;
                    case "E":
                        return 1.0;
                }
            case "CPF":
                switch (countryType) {
                    case "A":
                        return 0.6;
                    case "B":
                        return 0.7;
                    case "C":
                    case "D":
                    case "E":
                        return 1.0;
                }
            case "CorporateClients":
                switch (countryType) {
                    case "A":
                        return 0.6;
                    case "B":
                        return 0.7;
                    case "C":
                        return 0.8;
                    case "D":
                        return 0.9;
                    case "E":
                        return 1.0;
                }
        }
        return 0;
    }
}

