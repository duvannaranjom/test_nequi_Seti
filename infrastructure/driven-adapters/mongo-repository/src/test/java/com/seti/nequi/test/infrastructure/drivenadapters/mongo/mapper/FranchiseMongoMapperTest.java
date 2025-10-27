package com.seti.nequi.test.infrastructure.drivenadapters.mongo.mapper;

import com.seti.nequi.test.domain.model.entity.*;
import com.seti.nequi.test.infrastructure.drivenadapters.mongo.document.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseMongoMapperTest {
    @Test
    void domain_to_document_and_back() {
        Product p = Product.builder().id("p1").name("P").count(1).build();
        Branch b = Branch.builder().id("b1").name("B").products(List.of(p)).build();
        Franchise f = Franchise.builder().id("f1").name("F").branches(List.of(b)).build();

        FranchiseDocument doc = FranchiseMongoMapper.toDocument(f);
        Franchise round = FranchiseMongoMapper.toDomain(doc);

        assertEquals("f1", round.getId());
        assertEquals("F", round.getName());
        assertEquals("B", round.getBranches().get(0).getName());
    }
}
