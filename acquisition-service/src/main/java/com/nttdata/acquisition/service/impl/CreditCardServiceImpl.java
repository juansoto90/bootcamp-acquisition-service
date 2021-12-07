package com.nttdata.acquisition.service.impl;

import com.nttdata.acquisition.model.entity.CreditCard;
import com.nttdata.acquisition.service.ICreditCardService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardServiceImpl implements ICreditCardService {

    private final WebClient.Builder webClientBuilder;
    private final String WEB_CLIENT_URL = "microservice.web.creditcard";
    private final String BASE;

    public CreditCardServiceImpl(WebClient.Builder webClientBuilder, Environment env) {
        this.webClientBuilder = webClientBuilder;
        BASE = env.getProperty(WEB_CLIENT_URL);
    }

    @Override
    public Flux<CreditCard> findByCustomerDocumentNumber(String documentNumber) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .get()
                .uri("/customer/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToFlux(CreditCard.class);
    }

    @Override
    public Mono<CreditCard> save(CreditCard creditCard) {
        return webClientBuilder
                .build()
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(creditCard)
                .retrieve()
                .bodyToMono(CreditCard.class);
    }
}
