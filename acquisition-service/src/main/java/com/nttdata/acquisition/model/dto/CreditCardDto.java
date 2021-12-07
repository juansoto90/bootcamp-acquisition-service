package com.nttdata.acquisition.model.dto;

import com.nttdata.acquisition.model.entity.Customer;
import lombok.Data;

@Data
public class CreditCardDto {
    private String productType;
    private String customerType;
    private String documentNumber;
    private double creditLine;
}
