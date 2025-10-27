package com.seti.nequi.test.domain.model.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BranchTest {
    @Test
    void builderAndAccessors() {
        Product p = Product.builder().id("p1").name("Prod").count(5).build();
        Branch b = Branch.builder().id("b1").name("Branch").products(List.of(p)).build();
        Branch b2 = new Branch("b2", "Branch",List.of(p));
        Franchise f = Franchise.builder().id("f1").name("Fran").branches(List.of(b,b2)).build();

        assertEquals("f1", f.getId());
        assertEquals("Fran", f.getName());
        assertEquals(2, f.getBranches().size());
        assertEquals("b1", f.getBranches().get(0).getId());
        assertEquals("p1", f.getBranches().get(0).getProducts().get(0).getId());
        assertEquals("b2", f.getBranches().get(1).getId());
    }
}
