package com.nttdata.acquisition.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {
    private String id;
    private double amount;
    private double payment;
    private Customer customer;
    private String status;

    private Acquisition acquisition;
}
