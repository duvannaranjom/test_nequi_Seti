package com.seti.nequi.test.domain.usecase.branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteBranchUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, String branchId) {
        requireNonBlank(franchiseId, "franchiseId");
        requireNonBlank(branchId, "branchId");

        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new DomainException("Franchise not found: " + franchiseId)))
                .map(fr -> {
                    boolean removed = fr.getBranches().removeIf(b -> branchId.equals(b.getId()));
                    if (!removed) {
                        throw new DomainException("Branch not found: " + branchId);
                    }
                    return fr;
                })
                .flatMap(repository::save);
    }
}
