package com.nttdata.acquisition.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {
    private String id;
    private String creditNumber;
    private double amount;
    private double paymentAmount;
    private String status;

    private Acquisition acquisition;
}
