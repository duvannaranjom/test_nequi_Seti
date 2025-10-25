package com.seti.nequi.test.domain.usecase.branch;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddBranchUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, Branch newBranch) {
        requireNonBlank(franchiseId, "franchiseId");
        requireNonNull(newBranch, "branch");
        requireNonBlank(newBranch.getId(), "branch.id");
        requireNonBlank(newBranch.getName(), "branch.name");

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new DomainException("Franchise not found: " + franchiseId)))
                .map(fr -> {
                    ensureBranchList(fr);
                    if (branchIdExists(fr, newBranch.getId())) {
                        throw new DomainException("Branch id already exists: " + newBranch.getId());
                    }
                    fr.getBranches().add(newBranch);
                    return fr;
                })
                .flatMap(repository::save);
    }
}
