package com.nttdata.acquisition.service;

import com.nttdata.acquisition.model.entity.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditCardService {
    public Flux<CreditCard> findByCustomerDocumentNumber(String documentNumber);
    public Mono<CreditCard> save(CreditCard creditCard);
}
