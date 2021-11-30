package com.nttdata.acquisition.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRule {
    private boolean personal;
    private boolean enterprise;

    private boolean maintenanceCommission;
    private boolean maximumMovementLimit;
    private Integer movementAmount;
    private Integer maximumPersonalAccount;
    private Integer maximumEnterpriseAccount;
    private Integer minimumHeadlines;
    private Integer minimumAuthorizedSigners;

    private Integer personalCreditAmount;
    private Integer enterpriseCreditAmount;
}
