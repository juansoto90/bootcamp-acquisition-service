package com.nttdata.acquisition.util;

public class CreditCardRule {
    private String customerType;
    private Integer creditCardAmount;
    public CreditCardRule(String customerType){
        this.customerType = customerType;
        this.creditCardAmount = customerType == "PERSONAL" ? Integer.MAX_VALUE : Integer.MAX_VALUE;
    }
}
