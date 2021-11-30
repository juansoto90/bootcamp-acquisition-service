package com.nttdata.acquisition.service;

import com.nttdata.acquisition.model.entity.Acquisition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAcquisitionService {
    public Mono<Acquisition> create(Acquisition acquisition);
    public Mono<Acquisition> update(Acquisition acquisition);
    public Mono<Void> deleteById(String id);
    public Flux<Acquisition> findByIdCustomer(String idCustomer);
}
