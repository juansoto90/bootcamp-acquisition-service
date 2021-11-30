package com.nttdata.acquisition.service.impl;

import com.nttdata.acquisition.model.entity.Customer;
import com.nttdata.acquisition.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final WebClient.Builder webClientBuilder;
    private static final String WEB_CLIENT_URL = "customer.web.url";
    private final String URI;

    @Autowired
    public CustomerServiceImpl(WebClient.Builder webClientBuilder, Environment env) {
        this.webClientBuilder = webClientBuilder;
        URI = env.getProperty(WEB_CLIENT_URL);
    }

    @Override
    public Mono<Customer> findById(String id) {
        return webClientBuilder.build().get().uri(URI + "/{id}", id)
                .retrieve().bodyToMono(Customer.class);
    }
}
