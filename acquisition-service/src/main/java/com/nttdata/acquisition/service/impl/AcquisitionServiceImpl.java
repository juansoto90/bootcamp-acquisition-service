package com.nttdata.acquisition.service.impl;

import com.nttdata.acquisition.model.entity.Acquisition;
import com.nttdata.acquisition.model.entity.Customer;
import com.nttdata.acquisition.repository.IAcquisitionRepository;
import com.nttdata.acquisition.service.IAcquisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AcquisitionServiceImpl implements IAcquisitionService {

    @Autowired
    private final IAcquisitionRepository iAcquisitionRepository;

    @Override
    public Mono<Acquisition> create(Acquisition acquisition) {
        return iAcquisitionRepository.save(acquisition);
    }

    @Override
    public Mono<Acquisition> update(Acquisition acquisition) {
        return iAcquisitionRepository.save(acquisition);
    }

    @Override
    public Mono<Void> delete(Acquisition acquisition) {
        return iAcquisitionRepository.delete(acquisition);
    }

}
