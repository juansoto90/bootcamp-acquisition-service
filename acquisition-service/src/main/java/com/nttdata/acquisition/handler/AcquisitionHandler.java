package com.nttdata.acquisition.handler;

import com.nttdata.acquisition.exception.AcquisitionException;
import com.nttdata.acquisition.exception.messageException;
import com.nttdata.acquisition.model.dto.AccountDto;
import com.nttdata.acquisition.model.entity.Account;
import com.nttdata.acquisition.model.entity.Acquisition;
import com.nttdata.acquisition.model.entity.Customer;
import com.nttdata.acquisition.service.IAccountService;
import com.nttdata.acquisition.service.IAcquisitionService;
import com.nttdata.acquisition.service.ICustomerService;
import com.nttdata.acquisition.util.AccountRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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

    public Mono<ServerResponse> findCustomerById(ServerRequest request){
        String id = request.pathVariable("idCustomer");
        //Mono<Customer> c = request.bodyToMono(Customer.class);
        return service.findByCustomerDocumentNumber(id)
                .collectList()
                .flatMap(listC -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(listC))
                .switchIfEmpty(Mono.error(new RuntimeException("error vacio")));
        /*return service.findAcquisitionByCustomer__Id(id)
                .collectList()
                .flatMap(listC -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(listC))
                .switchIfEmpty(Mono.error(new RuntimeException("error vacio")));*/

    }

    @PostMapping
    public Mono<ServerResponse> create(ServerRequest request) {
        /*Mono<Account> account = request.bodyToMono(AccountDto.class).map(dto -> modelMapper.map(dto, Account.class));
        Mono<Acquisition> acquisition = request.bodyToMono(AcquisitionDto.class).map(dto -> modelMapper.map(dto, Acquisition.class));*/

        Mono<AccountDto> dto = request.bodyToMono(AccountDto.class);
        Account account = new Account();
        Acquisition acquisition = new Acquisition();

        return dto
                .flatMap(a -> {
                    return iCustomerService.findById(a.getIdCustomer())
                            .map(c -> {
                                account.setBalance(a.getBalance());
                                account.setCustomerOwner(a.getCustomerOwner());
                                account.setCustomerAuthorizedSigner(a.getCustomerAuthorizedSigner());

                                acquisition.setProductType(a.getProductType());
                                acquisition.setCustomer(c);
                                acquisition.setStatus("CREATED");
                                return acquisition;
                            });
                })
                .flatMap(a -> {
                    return service.findByCustomerDocumentNumber(a.getCustomer().getDocumentNumber())
                            .filter(f -> f.getStatus().equals("CREATED"))
                            .filter(f -> f.getProductType().equals(a.getProductType()))
                            .count()
                            .flatMap(c -> {
                                AccountRule accRule = new AccountRule(a.getProductType(), a.getCustomer().getCustomerType());
                                if (accRule.getMaximumAccount() > Math.toIntExact(c)) {
                                    if (a.getCustomer().getCustomerType().equals("ENTERPRISE")){
                                        if (!(accRule.getMinimumHeadlines() <= account.getCustomerOwner().stream().count())){
                                            return Mono.error(
                                                    new WebClientResponseException(400,
                                                            messageException.maximumHeadlinesMessage(a.getCustomer().getCustomerType()),
                                                            null,null,null)
                                            );
                                        }
                                    } else {
                                        if (!(accRule.getMinimumHeadlines() <= account.getCustomerOwner().stream().count()
                                                && account.getCustomerOwner().stream().count() <= accRule.getMaximumHeadlines())){
                                            return Mono.error(
                                                    new WebClientResponseException(400,
                                                            messageException.maximumHeadlinesMessage(a.getCustomer().getCustomerType()),
                                                            null,null,null)
                                            );
                                        } else if (!(accRule.getMaximumAuthorizedSigners() >= account.getCustomerAuthorizedSigner().stream().count())){
                                            return Mono.error(
                                                    new WebClientResponseException(400,
                                                            messageException.maximumAuthorizedSignersMessage(a.getCustomer().getCustomerType()),
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
                                                        messageException.accountQuantityValidationMessage(a.getProductType(), a.getCustomer().getCustomerType()),
                                                        null,null,null)
                                                     );
                                }
                            });
                })
                .flatMap(a -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(a))
                .onErrorResume(AcquisitionException::errorHandler);
                /*.onErrorResume(error -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) error;
                    if (errorResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                        Map<String, Object> body = new HashMap<>();
                        body.put("error", "Error: ".concat(errorResponse.getMessage()));
                        body.put("timestamp", new Date());
                        body.put("status", errorResponse.getStatusCode().value());
                        return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(body);
                    } else if (errorResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
                        Map<String, Object> body = new HashMap<>();
                        body.put("error", "Error: ".concat(errorResponse.getStatusText()));
                        body.put("timestamp", new Date());
                        body.put("status", errorResponse.getStatusCode().value());
                        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(body);
                    }
                    return Mono.error(errorResponse);
                });*/
    }
}
