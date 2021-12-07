package com.nttdata.acquisition.service;

import com.nttdata.acquisition.model.entity.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAccountService {
    public Mono<Account> save(Account account);
    public Flux<Account> findAllByCustomerOwner(Account account);
    public Flux<Account> findByCustomerDocumentNumber(String documentNumber);
}
