package com.nttdata.acquisition.service;

import com.nttdata.acquisition.model.entity.Customer;
import reactor.core.publisher.Mono;

public interface ICustomerService {
    public Mono<Customer> findById(String id);
    public Mono<Customer> findByDocumentNumber(String documentNumber);
}
