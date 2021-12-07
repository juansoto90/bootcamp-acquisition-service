package com.nttdata.acquisition.config;

import com.nttdata.acquisition.handler.AcquisitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> routes(AcquisitionHandler handler){
        return route(POST("/acquisition/account"), handler::createAccount)
                .andRoute(POST("/acquisition/creditcard"), handler::createCreditCard);
    }
}
