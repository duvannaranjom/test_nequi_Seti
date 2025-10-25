package com.seti.nequi.test.domain.usecase.core;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.domain.model.exception.DomainException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class BaseUseCase {

    protected void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new DomainException("Field '" + fieldName + "' must not be blank");
        }
    }

    protected <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new DomainException("Field '" + fieldName + "' must not be null");
        }
        return value;
    }

    // --- Helpers para manipular el árbol Franchise -> Branch -> Product ---

    protected Branch findBranchOrThrow(Franchise franchise, String branchId) {
        requireNonBlank(branchId, "branchId");
        return franchise.getBranches().stream()
                .filter(b -> Objects.equals(b.getId(), branchId))
                .findFirst()
                .orElseThrow(() -> new DomainException("Branch not found: " + branchId));
    }

    protected Product findProductOrThrow(Branch branch, String productId) {
        requireNonBlank(productId, "productId");
        return branch.getProducts().stream()
                .filter(p -> Objects.equals(p.getId(), productId))
                .findFirst()
                .orElseThrow(() -> new DomainException("Product not found: " + productId));
    }

    protected void ensureBranchList(Franchise franchise) {
        if (franchise.getBranches() == null) {
            franchise.setBranches(new ArrayList<>());
        }
    }

    protected void ensureProductList(Branch branch) {
        if (branch.getProducts() == null) {
            branch.setProducts(new ArrayList<>());
        }
    }

    protected boolean branchIdExists(Franchise franchise, String branchId) {
        return Optional.ofNullable(franchise.getBranches())
                .orElseGet(List::of)
                .stream().anyMatch(b -> Objects.equals(b.getId(), branchId));
    }

    protected boolean productIdExists(Branch branch, String productId) {
        return Optional.ofNullable(branch.getProducts())
                .orElseGet(List::of)
                .stream().anyMatch(p -> Objects.equals(p.getId(), productId));
    }
}
