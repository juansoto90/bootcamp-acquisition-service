package com.nttdata.acquisition.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditCardRule {
    private String customerType;
    private Integer creditCardAmount;
    public CreditCardRule(String customerType){
        this.customerType = customerType;
        //this.creditCardAmount = customerType == "PERSONAL" ? Integer.MAX_VALUE : Integer.MAX_VALUE;
        this.creditCardAmount = customerType == "PERSONAL" ? 1 : 1;
    }
}
