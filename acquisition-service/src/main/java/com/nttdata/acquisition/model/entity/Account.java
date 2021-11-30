package com.nttdata.acquisition.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private String id;
    private double balance;
    private String accountNumber;
    private boolean maintenanceCommission;
    private boolean maximumMovementLimit;
    private Integer movementAmount;
    private String status;

    private Acquisition acquisition;
}
