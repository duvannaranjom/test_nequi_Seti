package com.seti.nequi.test.Nequi.Test.application.services;

import com.seti.nequi.test.Nequi.Test.application.ports.input.FranchiseReactiveService;
import com.seti.nequi.test.Nequi.Test.application.ports.output.FranchiseReactiveRepository;
import com.seti.nequi.test.Nequi.Test.domain.model.Branch;
import com.seti.nequi.test.Nequi.Test.domain.model.Franchise;
import com.seti.nequi.test.Nequi.Test.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FranchiseReactiveServiceImpl implements FranchiseReactiveService {

    private final FranchiseReactiveRepository franchiseReactiveRepository;

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchiseReactiveRepository.save(franchise);
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        return franchiseReactiveRepository.findAll();
    }

    @Override
    public Mono<Franchise> getFranchiseById(String id) {
        return franchiseReactiveRepository.findById(id);
    }

    @Override
    public Mono<Franchise> updateFranchise(String id, Franchise newFrenchise) {
        return franchiseReactiveRepository.findById(id)
                .flatMap(franchiseExistente -> {
                    newFrenchise.setId(id);
                    newFrenchise.setBranches(franchiseExistente.getBranches());
                    return franchiseReactiveRepository.save(newFrenchise);
                });
    }

    @Override
    public Mono<Void> deleteFranchise(String id) {
        return franchiseReactiveRepository.deleteById(id);
    }

    @Override
    public Mono<Franchise> addBranchToFranchise(String franchiseId, Branch branch) {
        return franchiseReactiveRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Franchise not found with ID: " + franchiseId)))
                .flatMap(franchise -> {
                    franchise.getBranches().add(branch);
                    return franchiseReactiveRepository.save(franchise);
                });
    }

    @Override
    public Mono<Franchise> deleteBranchToFranchise(String franchiseId, String branchId) {
        return franchiseReactiveRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    franchise.setBranches(
                            franchise.getBranches().stream()
                                    .filter(s -> !s.getId().equals(branchId))
                                    .collect(Collectors.toList())
                    );
                    return franchiseReactiveRepository.save(franchise);
                });
    }

    @Override
    public Mono<Franchise> addProductToBranch(String franchiseId, String branchId, Product product) {
        return franchiseReactiveRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Franchise not found with ID: " + franchiseId)))
                .flatMap(franchise -> {
                    Optional<Branch> branchOpt = franchise.getBranches().stream()
                            .filter(s -> s.getId().equals(branchId))
                            .findFirst();

                    if (branchOpt.isEmpty()) {
                        return Mono.error(new NoSuchElementException("Branch not found with ID: " + branchId));
                    }

                    Branch branch = branchOpt.get();
                    branch.getProducts().add(product);

                    return franchiseReactiveRepository.save(franchise);
                });
    }

    @Override
    public Mono<Franchise> deleteProductToBranch(String franchiseId, String branchId, String productId) {
        return franchiseReactiveRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    franchise.getBranches().stream()
                            .filter(s -> s.getId().equals(branchId))
                            .findFirst()
                            .ifPresent(branch -> {
                                branch.setProducts(
                                        branch.getProducts().stream()
                                                .filter(p -> !p.getId().equals(productId))
                                                .collect(Collectors.toList())
                                );
                            });
                    return franchiseReactiveRepository.save(franchise);
                });
    }

    @Override
    public Mono<Franchise> updateCountProduct(String franchiseId, String branchId, String productId, int newCount) {
        return franchiseReactiveRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    boolean update = false;

                    for (Branch branch : franchise.getBranches()) {
                        if (branch.getId().equals(branchId)) {
                            for (Product product : branch.getProducts()) {
                                if (product.getId().equals(productId)) {
                                    product.setCount(newCount);
                                    update = true;
                                    break;
                                }
                            }
                            if (update) {
                                break;
                            }
                        }
                    }

                    if (update) {
                        return franchiseReactiveRepository.save(franchise);
                    } else {
                        return Mono.error(new NoSuchElementException("No se pudo actualizar el stock"));
                    }
                });
    }

    @Override
    public Flux<Branch> getProductMostCountByBranch(String franchiseId) {
        return franchiseReactiveRepository.findById(franchiseId)
                .flatMapMany(franchise -> {
                    if (franchise.getBranches() == null || franchise.getBranches().isEmpty()) {
                        return Flux.empty();
                    }

                    return Flux.fromIterable(franchise.getBranches())
                            .flatMap(branch -> {
                                var products = branch.getProducts();
                                if (products == null || products.isEmpty()) {
                                    return Mono.empty();
                                }

                                // 1) Tomar el máximo count de la sucursal
                                var maxOpt = products.stream()
                                        .map(Product::getCount)
                                        .max(Integer::compareTo);

                                if (maxOpt.isEmpty()) {
                                    return Mono.empty();
                                }

                                int max = maxOpt.get();

                                // 2) Incluir TODOS los productos con ese máximo (por si hay empates)
                                List<Product> topProducts = products.stream()
                                        .filter(p -> p.getCount() == max)
                                        .toList();

                                return Mono.just(new Branch(
                                        branch.getId(),
                                        branch.getName(),
                                        topProducts
                                ));
                            });
                });
    }


    @Override
    public Mono<Franchise> updateNameFranchise(String id, String newName) {
        return franchiseReactiveRepository.findById(id)
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return franchiseReactiveRepository.save(franchise);
                });
    }

    @Override
    public Mono<Franchise> updateNameBranch(String franchiseId, String branchId, String newName) {
        return franchiseReactiveRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    boolean update = false;

                    for (Branch branch : franchise.getBranches()) {
                        if (branch.getId().equals(branchId)) {
                            branch.setName(newName);
                            update = true;
                            break;
                        }
                    }

                    if (update) {
                        return franchiseReactiveRepository.save(franchise);
                    } else {
                        return Mono.error(new NoSuchElementException("Sucursal con ID no encontrada: " + branchId));
                    }
                });
    }

    @Override
    public Mono<Franchise> updateNameProduct(String franchiseId, String branchId, String productId, String newName) {
        return franchiseReactiveRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    boolean update = false;

                    for (Branch branch : franchise.getBranches()) {
                        if (branch.getId().equals(branchId)) {
                            for (Product product : branch.getProducts()) {
                                if (product.getId().equals(productId)) {
                                    product.setName(newName);
                                    update = true;
                                    break;
                                }
                            }
                            if (update) {
                                break;
                            }
                        }
                    }

                    if (update) {
                        return franchiseReactiveRepository.save(franchise);
                    } else {
                        return Mono.error(new NoSuchElementException("No se pudo actualizar el nombre del producto"));
                    }
                });
    }
}
