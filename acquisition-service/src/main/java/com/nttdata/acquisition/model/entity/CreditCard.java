package com.nttdata.acquisition.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCard {
    private String id;
    private String cardNumber;
    private double consumption;
    private double creditLine;
    private int expirationMonth;
    private int expirationYear;
    private String cvv;
    private Customer customer;
    private String status;

    private Acquisition acquisition;
}
