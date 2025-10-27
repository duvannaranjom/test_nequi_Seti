package com.seti.nequi.test.domain.model.entity;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FranchiseTest {
    @Test
    void builderAndAccessors() {
        Product p = Product.builder().id("p1").name("Prod").count(5).build();
        Branch b = Branch.builder().id("b1").name("Branch").products(List.of(p)).build();
        Franchise f = Franchise.builder().id("f1").name("Fran").branches(List.of(b)).build();

        assertEquals("f1", f.getId());
        assertEquals("Fran", f.getName());
        assertEquals(1, f.getBranches().size());
        assertEquals("b1", f.getBranches().get(0).getId());
        assertEquals("p1", f.getBranches().get(0).getProducts().get(0).getId());
    }
}
