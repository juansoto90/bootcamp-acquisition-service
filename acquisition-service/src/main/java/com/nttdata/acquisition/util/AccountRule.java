package com.nttdata.acquisition.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountRule {
    private String accountType;
    private String customerType;
    private boolean maintenanceCommission;
    private boolean maximumMovementLimit;
    private Integer movementAmount;
    private Integer maximumAccount;
    private Integer minimumHeadlines;
    private Integer maximumHeadlines;
    private Integer minimumAuthorizedSigners;
    private Integer maximumAuthorizedSigners;

    public AccountRule(String accountType, String customerType){
        //accountType: CUENTA_AHORRO, CUENTA_CORRIENTE AND CUENTA_PLAZOFIJO
        //customerType: PERSONAL AND ENTERPRISE
        this.accountType = accountType;
        this.customerType = customerType;
        this.maintenanceCommission = accountType.equals("CUENTA_AHORRO") ? false : accountType.equals("CUENTA_CORRIENTE") ? true : false;
        this.maximumMovementLimit = accountType.equals("CUENTA_AHORRO") ? false : accountType.equals("CUENTA_CORRIENTE") ? true : false;
        this.movementAmount = accountType.equals("CUENTA_AHORRO") ? 50 : accountType.equals("CUENTA_CORRIENTE") ? Integer.MAX_VALUE : 1;
        this.maximumAccount = accountType.equals("CUENTA_AHORRO")    ? customerType.equals("PERSONAL") ? 1 : 0 :
                              accountType.equals("CUENTA_CORRIENTE") ? customerType.equals("PERSONAL") ? 1 : Integer.MAX_VALUE :
                                                                       customerType.equals("PERSONAL") ? 1 : 0;
        this.minimumHeadlines = customerType.equals("PERSONAL") ? 1 : 1;
        this.maximumHeadlines = customerType.equals("PERSONAL") ? 1 : Integer.MAX_VALUE;
        this.minimumAuthorizedSigners = customerType.equals("PERSONAL") ? 0 : 0;
        this.maximumAuthorizedSigners = customerType.equals("PERSONAL") ? 0 : Integer.MAX_VALUE;
    }
}
