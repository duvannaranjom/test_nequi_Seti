package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteFranchiseUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Void> execute(String franchiseId) {
        requireNonBlank(franchiseId, "franchiseId");
        return repository.deleteById(franchiseId);
    }
}
