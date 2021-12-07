package com.nttdata.acquisition.model.dto;

import com.nttdata.acquisition.model.entity.Customer;
import lombok.Data;

import java.util.List;

@Data
public class AccountDto {
    private double balance;
    private String productType;
    private String customerType;
    private String documentNumber;
    private List<String> customerOwner;
    private List<String> customerAuthorizedSigner;
}
