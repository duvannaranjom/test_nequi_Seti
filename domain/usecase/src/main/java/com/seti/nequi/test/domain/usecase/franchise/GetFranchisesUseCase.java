package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetFranchisesUseCase {

    private final FranchiseRepositoryPort repository;

    public Flux<Franchise> execute() {
        return repository.findAll();
    }
}
