package com.seti.nequi.test.Nequi.Test.application.ports.input;

import com.seti.nequi.test.Nequi.Test.domain.model.Branch;
import com.seti.nequi.test.Nequi.Test.domain.model.Franchise;
import com.seti.nequi.test.Nequi.Test.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseReactiveService {
    // Franchise operations
    Mono<Franchise> createFranchise(Franchise franchise);
    Flux<Franchise> getAllFranchises();
    Mono<Franchise> getFranchiseById(String id);
    Mono<Franchise> updateFranchise(String id, Franchise franchise);
    Mono<Void> deleteFranchise(String id);

    // Branch operations
    Mono<Franchise> addBranchToFranchise(String franchiseId, Branch branch);
    Mono<Franchise> deleteBranchToFranchise(String franchiseId, String branchId);

    // Product operations
    Mono<Franchise> addProductToBranch(String franchiseId, String branchId, Product product);
    Mono<Franchise> deleteProductToBranch(String franchiseId, String branchId, String productId);
    Mono<Franchise> updateCountProduct(String franchiseId, String branchId, String productId, int newCount);

    Flux<Branch> getProductMostCountByBranch(String franchiseId); //obtenerProductConMasStockPorBranch

    // Operaciones adicionales para actualizar nombres
    Mono<Franchise> updateNameFranchise(String id, String newName);
    Mono<Franchise> updateNameBranch(String franchiseId, String branchId, String newName);
    Mono<Franchise> updateNameProduct(String franchiseId, String branchId, String productId, String newName);
}
