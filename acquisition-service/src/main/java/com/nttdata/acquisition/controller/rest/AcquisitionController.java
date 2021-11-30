package com.nttdata.acquisition.controller.rest;

import com.nttdata.acquisition.model.entity.Acquisition;
import com.nttdata.acquisition.model.entity.Customer;
import com.nttdata.acquisition.service.IAcquisitionService;
import com.nttdata.acquisition.service.ICustomerService;
import com.nttdata.acquisition.util.AccountRule;
import com.nttdata.acquisition.util.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/acquisition")
@RequiredArgsConstructor
public class AcquisitionController {

    private final IAcquisitionService iAcquisitionService;
    private final ICustomerService iCustomerService;

    private static final Logger Log = LoggerFactory.getLogger(AcquisitionController.class);

    @GetMapping("/{id}")
    public Mono<Long> findbyIdCustomer(@PathVariable String id){
        return iAcquisitionService.findByIdCustomer(id)
                .filter(f -> f.getStatus().equals("CREATED"))
                .count();
    }

    @PostMapping
    public Mono<Acquisition> create(@RequestBody Acquisition acquisition){
        //return iAcquisitionService.create(acquisition);
        AccountRule accountRule = new AccountRule();
        Validator validator = new Validator();

        //Mono<Acquisition> acquisitionMono =
        return
        iCustomerService.findById(acquisition.getIdCustomer())
        .flatMap(p -> {
            acquisition.setCustomer(p);
            return Mono.just(acquisition);
        })
        .doOnNext(acq -> {
            AccountRule acc = new AccountRule(acq.getProductType(), acq.getCustomer().getCustomerType());
            accountRule.setAccountType(acc.getAccountType());
            accountRule.setCustomerType(acc.getCustomerType());
            accountRule.setMaintenanceCommission(acc.isMaintenanceCommission());
            accountRule.setMaximumMovementLimit(acc.isMaximumMovementLimit());
            accountRule.setMovementAmount(acc.getMovementAmount());
            accountRule.setMaximumAccount(acc.getMaximumAccount());
            accountRule.setMinimumHeadlines(acc.getMinimumHeadlines());
            accountRule.setMaximumHeadlines(acc.getMaximumHeadlines());
            accountRule.setMinimumAuthorizedSigners(acc.getMinimumAuthorizedSigners());
            accountRule.setMaximumAuthorizedSigners(acc.getMaximumAuthorizedSigners());
        })
        .flatMap(acq -> {
            return  iAcquisitionService.findByIdCustomer(acq.getIdCustomer())
                    .filter(f -> f.getStatus().equals("CREATED"))
                    .count();
                    /*.map(c -> {
                        validator.setAmount(c.intValue());
                        return Mono.just(0);
                    });*/
                    /*.doOnNext(c -> {
                        validator.setAmount(Math.toIntExact(c));
                    });*/
                    //.subscribe(p -> System.out.println(p));
        })
        .flatMap(c -> {
            if (accountRule.getMaximumAccount() > Math.toIntExact(c)){
                return iAcquisitionService.create(acquisition);
            } else {
                return Mono.error(new RuntimeException(String.format("The client has an open account.")));
            }
        });
    }

    @PutMapping
    public Mono<Acquisition> update(Acquisition acquisition){
        return iAcquisitionService.update(acquisition);
    }

    @DeleteMapping
    public Mono<Void> deleteById(String id){
        return iAcquisitionService.deleteById(id);
    }
}
