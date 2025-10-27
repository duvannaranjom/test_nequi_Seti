package com.seti.nequi.test.domain.usecase.core;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.domain.model.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseUseCaseTest {

    static class Dummy extends BaseUseCase {}

    private final Dummy dummy = new Dummy();

    @Test
    void requireNonBlank_throws() {
        DomainException ex = assertThrows(DomainException.class, () -> dummy.requireNonBlank(" ", "name"));
        assertTrue(ex.getMessage().contains("name"));
    }

    @Test
    void requireNonNull_throws() {
        DomainException ex = assertThrows(DomainException.class, () -> dummy.requireNonNull(null, "obj"));
        assertTrue(ex.getMessage().contains("obj"));
    }

    @Test
    void findBranchOrThrow_ok() {
        Franchise f = Franchise.builder()
                .branches(List.of(Branch.builder().id("b1").name("B1").products(List.of()).build()))
                .build();
        Branch b = dummy.findBranchOrThrow(f, "b1");
        assertEquals("b1", b.getId());
    }

    @Test
    void findBranchOrThrow_notFound() {
        Franchise f = Franchise.builder().branches(List.of()).build();
        assertThrows(DomainException.class, () -> dummy.findBranchOrThrow(f, "x"));
    }

    @Test
    void findProductOrThrow_ok() {
        Branch b = Branch.builder()
                .products(List.of(Product.builder().id("p1").name("P1").count(1).build()))
                .build();
        Product p = dummy.findProductOrThrow(b, "p1");
        assertEquals("p1", p.getId());
    }
}
