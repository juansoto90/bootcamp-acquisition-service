package com.nttdata.acquisition.service;

import com.nttdata.acquisition.model.entity.Acquisition;
import com.nttdata.acquisition.model.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAcquisitionService {
    public Flux<Acquisition> findAcquisitionByCustomer_Id(String id);
    public Flux<Acquisition> findByCustomerDocumentNumber(String documentNumber);

    public Flux<Acquisition> findByIdCustomer(Customer customer);
    public Mono<Acquisition> create(Acquisition acquisition);
    public Mono<Acquisition> update(Acquisition acquisition);
    public Mono<Void> delete(Acquisition acquisition);
}
