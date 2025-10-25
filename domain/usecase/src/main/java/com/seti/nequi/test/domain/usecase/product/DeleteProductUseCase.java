package com.seti.nequi.test.domain.usecase.product;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteProductUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId) {
        requireNonBlank(franchiseId, "franchiseId");
        requireNonBlank(branchId, "branchId");
        requireNonBlank(productId, "productId");

        return repository.findById(franchiseId)
                .map(fr -> {
                    Branch branch = findBranchOrThrow(fr, branchId);
                    boolean removed = branch.getProducts().removeIf(p -> productId.equals(p.getId()));
                    if (!removed) {
                        throw new DomainException("Product not found: " + productId);
                    }
                    return fr;
                })
                .flatMap(repository::save);
    }
}
