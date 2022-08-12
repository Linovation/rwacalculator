package com.example.backend.rest;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    CalculatorResult calculate(CalculatorInput calculatorInput) {
        CalculatorResult calculatorResult = new CalculatorResult();
        ClientTypeStaticData clientTypeStaticData = new ClientTypeStaticData();
        clientTypeStaticData.setClientType(calculatorInput.getTypeOfCustomer());
        calculatorResult.setEad(setEadValue(calculatorInput));
        calculatorResult.setCost(setCost(calculatorResult.getEad()));
        calculatorResult.setNpvRecSecCash(setNpvRecSecCash(calculatorInput));
        calculatorResult.setNpvRecUnSecCash(setNpvRecUnSecCash(calculatorInput, calculatorResult.getEad()));
        calculatorResult.setLgd(setLGD(calculatorResult.getEad(), calculatorResult.getCost(), calculatorResult.getNpvRecUnSecCash(),
                calculatorResult.getNpvRecSecCash(), calculatorInput));
        calculatorResult.setRwaCash(setRwa(calculatorInput, calculatorResult.getLgd(), calculatorResult.getEad()));

        calculatorResult.setNpvRecSecCashGuar(setNpvRecoverySecCashGuar(calculatorInput, getRecoveryValueCashGuar(calculatorInput, calculatorResult.getEad())));
        calculatorResult.setNpvRecUnSecCashGuar(setNpvRecoveryUnSecCashGuar(calculatorInput, calculatorInput.getCreditBalance(),
                getRecoveryValueCashGuar(calculatorInput, calculatorResult.getEad()), calculatorResult.getEad()));
        calculatorResult.setLgdCashGuar(setLgdGuarantee(calculatorInput, calculatorResult.getNpvRecSecCash(), calculatorResult.getNpvRecSecCashGuar()
                , calculatorResult.getNpvRecUnSecCashGuar(), calculatorResult.getEad(), calculatorResult.getCost()));
        calculatorResult.setRwaCashGuar(setRwaCashGuar(calculatorInput, calculatorResult.getLgdCashGuar(), calculatorResult.getEad()));
        return calculatorResult;
    }

    Double setEadValue(CalculatorInput calculatorInput) {
        Double ead;
        if (calculatorInput.getDebitBalance() < calculatorInput.getLimit()) {
            ead = calculatorInput.getDebitBalance() +
                    (calculatorInput.getLimit() - calculatorInput.getDebitBalance()) * ClientTypeStaticData.getkFactorU();
        } else {
            ead = calculatorInput.getDebitBalance() * (1 + ClientTypeStaticData.geteFactorOs());
        }
        return ead;
    }

    Double setCost(Double ead) {
        Double cost;
        if (ead * ClientTypeStaticData.getCost() > ClientTypeStaticData.getCostCap()) {
            cost = ClientTypeStaticData.getCostCap() / ead;
        } else {
            cost = ClientTypeStaticData.getCost();
        }
        return cost;
    }

    Double setNpvRecSecCash(CalculatorInput calculatorInput) {
        Double npvRecSecCash;
        if (calculatorInput.getCreditBalance() == 0) {
            npvRecSecCash = 0.0;
        } else if (calculatorInput.isFxMismatch()) {
            npvRecSecCash = calculatorInput.getCreditBalance()
                    * (1 - ClientTypeStaticData.getFxMismatchHairCut()) * ClientTypeStaticData.getDisFactorCash();
        } else {
            npvRecSecCash = calculatorInput.getCreditBalance()
                    * (1 - ClientTypeStaticData.getNoFxMismatchHairCut()) * ClientTypeStaticData.getDisFactorCash();
        }
        return npvRecSecCash;
    }

    Double setNpvRecUnSecCash(CalculatorInput calculatorInput, Double ead) {
        double npvRecUnSecCash = (ead - calculatorInput.getCreditBalance())
                * (1 - ClientTypeStaticData.gethFactorUnsecured(calculatorInput.getCountry().getClassification())) * ClientTypeStaticData.getDiscountFactorGen();
        return npvRecUnSecCash > 0 ? npvRecUnSecCash : 0;
    }

    Double setLGD(Double ead, Double cost, Double npvRecUnSecCash, Double npvRecSecCash, CalculatorInput calculatorInput) {
        Double lgd;
        if (ead == 0) {
            lgd = 0.0;
        } else if (ead - (npvRecUnSecCash + npvRecSecCash) > 0) {
            lgd = ((ead - (npvRecUnSecCash + npvRecSecCash)) * (1 - ClientTypeStaticData.getNoLossRate(calculatorInput.getCountry().getClassification())) / ead
                    + cost) * ClientTypeStaticData.getDownturnFactor();
        } else {
            lgd = cost * ClientTypeStaticData.getDownturnFactor();
        }
        return lgd;
    }

    Double setRwa(CalculatorInput calculatorInput, Double lgd, Double ead) {
        Double rwa;
        NormalDistribution normDist = new NormalDistribution();
        Double pd = Double.parseDouble(calculatorInput.getRating().getPd());
        Double correlationR = 0.12 * (1 - Math.exp(-50 * pd)) + 0.24 * (1 - (1 - Math.exp(-50 * pd)) / (1 - Math.exp(-50)));
        Double maturityAdjustment = Math.pow(0.11852 - 0.05478 * Math.log(pd), 2);
        Double capitalRequirement = (lgd * normDist.cumulativeProbability(Math.pow(1 - correlationR, -0.5) * normDist.inverseCumulativeProbability(pd) +
                Math.pow(correlationR / (1 - correlationR), 0.5) * normDist.inverseCumulativeProbability(0.999)) - pd * lgd)
                * Math.pow(1 - 1.5 * maturityAdjustment, -1) * (1 + (ClientTypeStaticData.getMaturity() - 2.5) * maturityAdjustment) * 1.06;
        rwa = capitalRequirement * 12.5 * ead;
        return rwa;
    }

    Double getRecoveryValueCashGuar(CalculatorInput calculatorInput, Double ead) {
        if (calculatorInput.getIngGuarantee() == 0) {
            return 0.0;
        }
        return ead < calculatorInput.getIngGuarantee() ? ead : calculatorInput.getIngGuarantee();
    }

    Double setNpvRecoverySecCashGuar(CalculatorInput calculatorInput, Double recoveryValueCashGuar) {
        Double discountFactor = ClientTypeStaticData.getDiscountFactorGen();
        Double hairCut = ClientTypeStaticData.getHFactorInternalGuarantee(calculatorInput.getCountry().getClassification());
        return recoveryValueCashGuar == 0 ? 0 : discountFactor * (1 - hairCut) * recoveryValueCashGuar;
    }

    Double setNpvRecoveryUnSecCashGuar(CalculatorInput calculatorInput, Double recoveryValueCashGuar, Double recoveryValueSecCash, Double ead) {
        Double npvRecoverySecCashGuar = (ead - recoveryValueSecCash - recoveryValueCashGuar)
                * (1 - ClientTypeStaticData.gethFactorUnsecured(calculatorInput.getCountry().getClassification())) *
                ClientTypeStaticData.getDiscountFactorGen();
        return npvRecoverySecCashGuar < 0 ? 0 : npvRecoverySecCashGuar;
    }

    Double setLgdGuarantee(CalculatorInput calculatorInput, Double npvRecoverySecCash, Double npvRecoverySecCashGuar,
                           Double npvRecoveryUnSecCashGuar, Double ead, Double cost) {
        Double lgd;
        if (ead == 0) {
            lgd = 0.0;
        } else if (ead - (npvRecoverySecCashGuar + npvRecoverySecCashGuar + npvRecoverySecCash) > 0) {
            lgd = ((ead - (npvRecoverySecCashGuar + npvRecoveryUnSecCashGuar + npvRecoverySecCash)) *
                    (1 - ClientTypeStaticData.getNoLossRate(calculatorInput.getCountry().getClassification())) / ead
                    + cost) * ClientTypeStaticData.getDownturnFactor();
        } else {
            lgd = cost * ClientTypeStaticData.getDownturnFactor();
        }
        return lgd;
    }

    Double setRwaCashGuar(CalculatorInput calculatorInput, Double lgdCash, Double ead) {
        Double rwa;
        NormalDistribution normDist = new NormalDistribution();
        Double pd = Double.parseDouble(calculatorInput.getRating().getPd());
        Double correlationR = 0.12 * (1 - Math.exp(-50 * pd)) + 0.24 * (1 - (1 - Math.exp(-50 * pd)) / (1 - Math.exp(-50)));
        Double maturityAdjustment = Math.pow(0.11852 - 0.05478 * Math.log(pd), 2);
        Double capitalRequirement = (lgdCash * normDist.cumulativeProbability(Math.pow(1 - correlationR, -0.5) * normDist.inverseCumulativeProbability(pd) +
                Math.pow(correlationR / (1 - correlationR), 0.5) * normDist.inverseCumulativeProbability(0.999)) - pd * lgdCash)
                * Math.pow(1 - 1.5 * maturityAdjustment, -1) * (1 + (ClientTypeStaticData.getMaturity() - 2.5) * maturityAdjustment) * 1.06;
        rwa = capitalRequirement * 12.5 * ead;
        return rwa;
    }


}
