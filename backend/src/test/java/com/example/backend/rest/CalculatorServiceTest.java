//package com.example.backend.rest;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class CalculatorServiceTest {
//
//    @Mock
//    ClientTypeStaticData clientTypeStaticData;
//
//    @Test
//    public void calculateNpvRecUnSecCash() {
//        CalculatorService calculatorService = new CalculatorService();
//
//        Mockito.when(clientTypeStaticData.getClientType().toString()).thenReturn("CorporateClients");
//        Double npvRecUnSecCash = calculatorService.setNpvRecUnSecCash(setupTestInputData(), 1.7203223E7);
//        Assertions.assertEquals(4589452, npvRecUnSecCash);
//    }
//
//    private CalculatorInput setupTestInputData() {
//        CalculatorInput calculatorInput = new CalculatorInput();
//        calculatorInput.setDebitBalance(17203223.0);
//        calculatorInput.setCreditBalance(4858401.0);
//        calculatorInput.setFxMismatch(true);
//        calculatorInput.setLimit(5000000.0);
//        calculatorInput.setIngGuarantee(5000000.0);
//        calculatorInput.setTypeOfCustomer("CorporateClients");
//        calculatorInput.setHurdleReturn(0.0);
//        Country country = new Country();
//        country.setLabel("Netherlands");
//        country.setClassification("A");
//        calculatorInput.setCountry(country);
//        Rating rating = new Rating();
//        rating.setLabel("CL10");
//        rating.setPd("0.00307018");
//        rating.setLabelDescription("CL10");
//        calculatorInput.setRating(rating);
//        return calculatorInput;
//    }
//}