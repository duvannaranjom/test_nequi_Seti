package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, String newName) {
        requireNonBlank(franchiseId, "franchiseId");
        requireNonBlank(newName, "newName");
        return repository.findById(franchiseId)
                .map(fr -> {
                    fr.setName(newName);
                    return fr;
                })
                .flatMap(repository::save);
    }
}
