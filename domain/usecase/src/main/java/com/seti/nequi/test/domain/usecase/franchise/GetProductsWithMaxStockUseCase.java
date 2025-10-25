package com.seti.nequi.test.domain.usecase.franchise;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetProductsWithMaxStockUseCase {

    private final FranchiseRepositoryPort repository;

    public Flux<Branch> execute(String franchiseId) {
        return repository.findById(franchiseId)
                .flatMapMany(franchise -> Flux.fromIterable(
                        franchise.getBranches().stream()
                                .map(branch -> {
                                    Product maxProduct = branch.getProducts().stream()
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
