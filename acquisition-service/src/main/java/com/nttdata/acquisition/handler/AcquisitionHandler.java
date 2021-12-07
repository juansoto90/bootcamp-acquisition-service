package com.nttdata.acquisition.handler;

import com.nttdata.acquisition.exception.AcquisitionException;
import com.nttdata.acquisition.exception.messageException;
import com.nttdata.acquisition.model.dto.AccountDto;
import com.nttdata.acquisition.model.dto.CreditCardDto;
import com.nttdata.acquisition.model.entity.Account;
import com.nttdata.acquisition.model.entity.Acquisition;
import com.nttdata.acquisition.model.entity.CreditCard;
import com.nttdata.acquisition.model.entity.Customer;
import com.nttdata.acquisition.service.IAccountService;
import com.nttdata.acquisition.service.IAcquisitionService;
import com.nttdata.acquisition.service.ICreditCardService;
import com.nttdata.acquisition.service.ICustomerService;
import com.nttdata.acquisition.util.AccountRule;
import com.nttdata.acquisition.util.CreditCardRule;
import com.nttdata.acquisition.util.CreditRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AcquisitionHandler {

    @Autowired
    private final IAcquisitionService service;
    @Autowired
    private final ICustomerService iCustomerService;
    @Autowired
    private final IAccountService iAccountService;
    private final ICreditCardService iCreditCardService;

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        Mono<AccountDto> dto = request.bodyToMono(AccountDto.class);
        Account account = new Account();
        Acquisition acquisition = new Acquisition();

        return dto
                .flatMap(d -> iCustomerService.findByDocumentNumber(d.getDocumentNumber())
                                                    .map(c -> {
                                                        account.setCustomer(c);
                                                        return d;
                                                    })
                )
                .flatMap(d -> d.getCustomerOwner() == null ?
                                                Mono.just(d) :
                                                Flux.fromIterable(d.getCustomerOwner())
                                                                  .flatMap(c ->  iCustomerService.findByDocumentNumber(c))
                                                                  .collectList()
                                                                  .map(list -> {
                                                                        account.setCustomerOwner(list);
                                                                        return d;
                                                                  })
                )
                .flatMap(d -> d.getCustomerAuthorizedSigner() == null ?
                                                Mono.just(d) :
                                                Flux.fromIterable(d.getCustomerAuthorizedSigner())
                                                                   .flatMap(c ->  iCustomerService.findByDocumentNumber(c))
                                                                   .collectList()
                                                                   .map(list -> {
                                                                        account.setCustomerAuthorizedSigner(list);
                                                                        return d;
                                                                   })
                )
                .map(a -> {
                    account.setBalance(a.getBalance());

                    acquisition.setProductType(a.getProductType());
                    acquisition.setCustomerType(a.getCustomerType());
                    acquisition.setStatus("CREATED");
                    return acquisition;
                })
                .flatMap(a -> {
                    return iAccountService.findByCustomerDocumentNumber(account.getCustomer().getDocumentNumber())
                            .filter(f -> f.getStatus().equals("CREATED"))
                            .filter(f -> f.getAcquisition().getProductType().equals(a.getProductType()))
                            .count()
                            .flatMap(c -> {
                                AccountRule accRule = new AccountRule(a.getProductType(), a.getCustomerType());
                                if (accRule.getMaximumAccount() > Math.toIntExact(c)) {
                                    if (a.getCustomerType().equals("ENTERPRISE")){
                                        if (!(accRule.getMinimumHeadlines() <= account.getCustomerOwner().stream().count())){
                                            return Mono.error(
                                                    new WebClientResponseException(400,
                                                            messageException.maximumHeadlinesMessage(a.getCustomerType()),
                                                            null,null,null)
                                            );
                                        }
                                    } else {
                                        if (!(accRule.getMinimumHeadlines() <= account.getCustomerOwner().stream().count()
                                                && account.getCustomerOwner().stream().count() <= accRule.getMaximumHeadlines())){
                                            return Mono.error(
                                                    new WebClientResponseException(400,
                                                            messageException.maximumHeadlinesMessage(a.getCustomerType()),
                                                            null,null,null)
                                            );
                                        } else if (!(accRule.getMaximumAuthorizedSigners() >= Math.toIntExact(account.getCustomerAuthorizedSigner() == null ? 0 : account.getCustomerAuthorizedSigner().stream().count()))){
                                            return Mono.error(
                                                    new WebClientResponseException(400,
                                                            messageException.maximumAuthorizedSignersMessage(a.getCustomerType()),
                                                            null,null,null)
                                            );
                                        }
                                    }
                                    return service.create(a)
                                            .flatMap(aq -> {
                                                account.setAccountNumber(UUID.randomUUID().toString());
                                                account.setMaintenanceCommission(accRule.isMaintenanceCommission());
                                                account.setMaximumMovementLimit(accRule.isMaximumMovementLimit());
                                                account.setMovementAmount(accRule.getMovementAmount());
                                                account.setStatus("CREATED");
                                                account.setAcquisition(a);
                                                return iAccountService.save(account);
                                            });
                                } else {
                                    return Mono.error(
                                            new WebClientResponseException(400,
                                                    messageException.accountQuantityValidationMessage(a.getProductType(), a.getCustomerType()),
                                                    null,null,null)
                                    );
                                }
                            });
                })
                .flatMap(a -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(a))
                .onErrorResume(AcquisitionException::errorHandler);
    }

    public Mono<ServerResponse> createCreditCard(ServerRequest request){
        Mono<CreditCardDto> dto = request.bodyToMono(CreditCardDto.class);
        CreditCard creditCard = new CreditCard();
        Acquisition acquisition = new Acquisition();
        return dto
                .flatMap(d -> iCustomerService.findByDocumentNumber(d.getDocumentNumber())
                        .map(c -> {
                            creditCard.setCustomer(c);
                            creditCard.setConsumption(0);
                            creditCard.setCreditLine(d.getCreditLine());
                            return d;
                        })
                )
                .map(d -> {
                    acquisition.setProductType(d.getProductType());
                    acquisition.setCustomerType(d.getCustomerType());
                    acquisition.setStatus("CREATED");
                    return acquisition;
                })
                .flatMap(a -> {
                    return iCreditCardService.findByCustomerDocumentNumber(creditCard.getCustomer().getDocumentNumber())
                            .filter(f -> f.getStatus().equals("CREATED"))
                            .filter(f -> f.getAcquisition().getProductType().equals(a.getProductType()))
                            .count()
                            .flatMap(c -> {
                                CreditCardRule creditCardRule = new CreditCardRule(a.getCustomerType());
                                if (creditCardRule.getCreditCardAmount() <= Math.toIntExact(c)) {
                                    return Mono.error(
                                            new WebClientResponseException(400,
                                                    messageException.creditCardAmountMessage(),
                                                    null,null,null)
                                    );
                                }
                                return service.create(a)
                                        .flatMap(aq -> {
                                            creditCard.setStatus("CREATED");
                                            creditCard.setAcquisition(a);
                                            return iCreditCardService.save(creditCard);
                                        });
                            });
                })
                .flatMap(a -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(a))
                .onErrorResume(AcquisitionException::errorHandler);
    }
}
