package com.nttdata.acquisition.repository;

import com.nttdata.acquisition.model.entity.Acquisition;
import com.nttdata.acquisition.model.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IAcquisitionRepository extends ReactiveMongoRepository<Acquisition, String> {
    public Flux<Acquisition> findAcquisitionByCustomer_Id(String id);
    Flux<Acquisition> findByCustomerDocumentNumber(String documentNumber);
    public Flux<Acquisition> findByCustomer(Customer customer);
}
