package com.seti.nequi.test.domain.usecase.branch;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateBranchNameUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, String branchId, String newName) {
        requireNonBlank(franchiseId, "franchiseId");
        requireNonBlank(branchId, "branchId");
        requireNonBlank(newName, "newName");

        return repository.findById(franchiseId)
                .map(fr -> {
                    Branch b = findBranchOrThrow(fr, branchId);
                    b.setName(newName);
                    return fr;
                })
                .flatMap(repository::save);
    }
}
