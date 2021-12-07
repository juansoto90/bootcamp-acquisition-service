package com.nttdata.acquisition.service.impl;

import com.nttdata.acquisition.model.entity.Credit;
import com.nttdata.acquisition.service.ICreditService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements ICreditService {

    private final WebClient.Builder webClientBuilder;
    private final String WEB_CLIENT_URL = "microservice.web.credit";
    private final String BASE;

    public CreditServiceImpl(WebClient.Builder webClientBuilder, Environment env) {
        this.webClientBuilder = webClientBuilder;
        this.BASE = env.getProperty(WEB_CLIENT_URL);
    }

    @Override
    public Flux<Credit> findByCustomerDocumentNumber(String documentNumber) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .get()
                .uri("/customer/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToFlux(Credit.class);
    }

    @Override
    public Mono<Credit> save(Credit credit) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(credit)
                .retrieve()
                .bodyToMono(Credit.class);
    }
}
