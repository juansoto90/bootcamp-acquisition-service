package com.nttdata.acquisition.service;

import com.nttdata.acquisition.model.entity.Account;
import reactor.core.publisher.Mono;

public interface IAccountService {
    public Mono<Account> save(Account account);
}
