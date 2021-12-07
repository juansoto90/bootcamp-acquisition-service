package com.nttdata.acquisition.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditRule {
    private String customerType;
    private Integer creditAmount;
    public CreditRule(String customerType){
        this.customerType = customerType;
        this.creditAmount = customerType == "PERSONAL" ? 1 : Integer.MAX_VALUE;
    }
}
