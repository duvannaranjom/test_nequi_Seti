package com.seti.nequi.test.domain.usecase.report;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.domain.model.exception.DomainException;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.usecase.core.BaseUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetProductsWithMaxStockByBranchUseCase extends BaseUseCase {

    private final FranchiseRepositoryPort repository;

    public Flux<Branch> execute(String franchiseId) {
        requireNonBlank(franchiseId, "franchiseId");

        // 👇 Cambia Flux.error(...) por Mono.error(...)
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new DomainException("Franchise not found: " + franchiseId)))
                .flatMapMany(fr -> Flux.fromIterable(
                        fr.getBranches().stream()
                                .map(branch -> {
                                    Product maxProduct = branch.getProducts() == null || branch.getProducts().isEmpty()
                                            ? null
                                            : branch.getProducts().stream()
                                            .max(Comparator.comparing(Product::getCount))
                                            .orElse(null);
                                    return Branch.builder()
                                            .id(branch.getId())
                                            .name(branch.getName())
                                            .products(maxProduct != null ? List.of(maxProduct) : List.of())
                                            .build();
                                })
                                .collect(Collectors.toList())
                ));
    }
}
