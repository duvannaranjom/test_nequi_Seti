package com.seti.nequi.test.domain.usecase.product;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateProductCountUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId, int newCount) {
        requireNonBlank(franchiseId, "franchiseId");
        requireNonBlank(branchId, "branchId");
        requireNonBlank(productId, "productId");

        if (newCount < 0) {
            throw new com.seti.nequi.test.domain.model.exception.DomainException("product.count must be >= 0");
        }

        return repository.findById(franchiseId)
                .map(fr -> {
                    Branch branch = findBranchOrThrow(fr, branchId);
                    Product p = findProductOrThrow(branch, productId);
                    p.setCount(newCount);
                    return fr;
                })
                .flatMap(repository::save);
    }
}
