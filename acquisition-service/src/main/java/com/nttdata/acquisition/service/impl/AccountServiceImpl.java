package com.nttdata.acquisition.service.impl;

import com.nttdata.acquisition.model.entity.Account;
import com.nttdata.acquisition.model.entity.Customer;
import com.nttdata.acquisition.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private final WebClient.Builder webClientBuilder;
    private static final String WEB_CLIENT_URL = "microservice.web.account";
    private final String BASE;

    public AccountServiceImpl(WebClient.Builder webClientBuilder, Environment env) {
        this.webClientBuilder = webClientBuilder;
        BASE = env.getProperty(WEB_CLIENT_URL);
    }

    @Override
    public Mono<Account> save(Account account) {
        /*return webClientBuilder.build().post().uri(URI)
                .retrieve().bodyToMono(Account.class);*/
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .post()
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .retrieve()
                .bodyToMono(Account.class);
    }

    @Override
    public Flux<Account> findAllByCustomerOwner(Account account) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .post()
                .uri("/customerowner")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .retrieve()
                .bodyToFlux(Account.class);
    }

    @Override
    public Flux<Account> findByCustomerDocumentNumber(String documentNumber) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .get()
                .uri("/customer/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToFlux(Account.class);
    }
}
