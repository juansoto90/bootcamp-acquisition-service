package com.nttdata.acquisition.exception;

public class messageException {
    private String productType;
    private String customerType;

    public static String accountQuantityValidationMessage(String productType, String customerType){
        if (customerType.equals("PERSONAL")){
            if (productType.equals("CUENTA_AHORRO"))
                return "The client already has a savings account";
            else if (productType.equals("CUENTA_CORRIENTE"))
                return "The client already has a current account";
            else
                return "The client already has a fixed-term account";
        }
        else
            if (productType.equals("CUENTA_AHORRO"))
                return "This customer cannot have a savings account";
            else
                return "This customer cannot have a fixed-term account";
    }

    public static String maximumHeadlinesMessage(String customerType){
        if (customerType.equals("PERSONAL"))
            return "Account must have a holder";
        else
            return "Account must have at least one owner";
    }

    public static String maximumAuthorizedSignersMessage(String customerType){
        if (customerType.equals("PERSONAL"))
            return "Authorized signers are not required";
        else
            return "";
    }

    public static String holderQuantityMessage(){
        return "Account must have at least one owner";
    }

    public static String creditCardAmountMessage(){
        return "You already have a credit card";
    }
}
