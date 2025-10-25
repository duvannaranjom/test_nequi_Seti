package com.seti.nequi.test.domain.usecase.product;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddProductUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, String branchId, Product newProduct) {
        requireNonBlank(franchiseId, "franchiseId");
        requireNonBlank(branchId, "branchId");
        requireNonNull(newProduct, "product");
        requireNonBlank(newProduct.getId(), "product.id");
        requireNonBlank(newProduct.getName(), "product.name");

        return repository.findById(franchiseId)
                .map(fr -> {
                    Branch branch = findBranchOrThrow(fr, branchId);
                    ensureProductList(branch);
                    if (productIdExists(branch, newProduct.getId())) {
                        throw new com.seti.nequi.test.domain.model.exception.DomainException(
                                "Product id already exists in branch: " + newProduct.getId());
                    }
                    branch.getProducts().add(newProduct);
                    return fr;
                })
                .flatMap(repository::save);
    }
}
