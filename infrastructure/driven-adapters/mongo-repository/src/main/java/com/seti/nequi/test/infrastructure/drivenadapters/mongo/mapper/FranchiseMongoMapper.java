package com.seti.nequi.test.infrastructure.drivenadapters.mongo.mapper;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.infrastructure.drivenadapters.mongo.document.BranchDocument;
import com.seti.nequi.test.infrastructure.drivenadapters.mongo.document.FranchiseDocument;
import com.seti.nequi.test.infrastructure.drivenadapters.mongo.document.ProductDocument;

import java.util.List;
import java.util.stream.Collectors;

public final class FranchiseMongoMapper {

    private FranchiseMongoMapper(){}

    public static FranchiseDocument toDocument(Franchise f) {
        return FranchiseDocument.builder()
                .id(f.getId())
                .name(f.getName())
                .branches(f.getBranches() == null ? null :
                        f.getBranches().stream()
                                .map(FranchiseMongoMapper::toDoc)
                                .collect(Collectors.toList()))
                .build();
    }

    public static Franchise toDomain(FranchiseDocument d) {
        if (d == null) return null;
        return Franchise.builder()
                .id(d.getId())
                .name(d.getName())
                .branches(d.getBranches() == null ? null :
                        d.getBranches().stream()
                                .map(FranchiseMongoMapper::toDomain)
                                .collect(Collectors.toList()))
                .build();
    }

    private static BranchDocument toDoc(Branch b) {
        return BranchDocument.builder()
                .id(b.getId())
                .name(b.getName())
                .products(b.getProducts() == null ? null :
                        b.getProducts().stream().map(FranchiseMongoMapper::toDoc).collect(Collectors.toList()))
                .build();
    }

    private static Branch toDomain(BranchDocument b) {
        return Branch.builder()
                .id(b.getId())
                .name(b.getName())
                .products(b.getProducts() == null ? null :
                        b.getProducts().stream().map(FranchiseMongoMapper::toDomain).collect(Collectors.toList()))
                .build();
    }

    private static ProductDocument toDoc(Product p) {
        return ProductDocument.builder()
                .id(p.getId())
                .name(p.getName())
                .count(p.getCount())
                .build();
    }

    private static Product toDomain(ProductDocument p) {
        return Product.builder()
                .id(p.getId())
                .name(p.getName())
                .count(p.getCount())
                .build();
    }
}
