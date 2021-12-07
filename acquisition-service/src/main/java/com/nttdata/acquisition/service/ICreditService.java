package com.nttdata.acquisition.service;

import com.nttdata.acquisition.model.entity.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditService {
    public Flux<Credit> findByCustomerDocumentNumber(String documentNumber);
    public Mono<Credit> save(Credit credit);
}
