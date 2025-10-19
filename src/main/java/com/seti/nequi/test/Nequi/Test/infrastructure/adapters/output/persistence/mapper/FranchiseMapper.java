package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.mapper;

import com.seti.nequi.test.Nequi.Test.domain.model.Branch;
import com.seti.nequi.test.Nequi.Test.domain.model.Franchise;
import com.seti.nequi.test.Nequi.Test.domain.model.Product;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.entity.BranchEntity;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.entity.FranchiseEntity;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.output.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FranchiseMapper {

    public Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) {
            return null;
        }

        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getNombre())
                .branches(entity.getSucursales().stream()
                        .map(this::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public FranchiseEntity toEntity(Franchise domain) {
        if (domain == null) {
            return null;
        }

        return FranchiseEntity.builder()
                .id(domain.getId())
                .nombre(domain.getName())
                .sucursales(domain.getBranches().stream()
                        .map(this::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    private Branch toDomain(BranchEntity entity) {
        if (entity == null) {
            return null;
        }

        return Branch.builder()
                .id(entity.getId())
                .name(entity.getNombre())
                .products(entity.getProductos().stream()
                        .map(this::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    private BranchEntity toEntity(Branch domain) {
        if (domain == null) {
            return null;
        }

        return BranchEntity.builder()
                .id(domain.getId())
                .nombre(domain.getName())
                .productos(domain.getProducts().stream()
                        .map(this::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    private Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return Product.builder()
                .id(entity.getId())
                .name(entity.getNombre())
                .count(entity.getStock())
                .build();
    }

    private ProductEntity toEntity(Product domain) {
        if (domain == null) {
            return null;
        }

        return ProductEntity.builder()
                .id(domain.getId())
                .nombre(domain.getName())
                .stock(domain.getCount())
                .build();
    }
}
