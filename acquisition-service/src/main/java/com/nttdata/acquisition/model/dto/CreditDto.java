package com.nttdata.acquisition.model.dto;

import lombok.Data;

@Data
public class CreditDto {
    private String productType;
    private String customerType;
    private String documentNumber;
    private double amount;
}
