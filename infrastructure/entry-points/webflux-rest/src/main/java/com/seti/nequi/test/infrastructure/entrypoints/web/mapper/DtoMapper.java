package com.seti.nequi.test.infrastructure.entrypoints.web.mapper;

import com.seti.nequi.test.domain.model.entity.Branch;
import com.seti.nequi.test.domain.model.entity.Franchise;
import com.seti.nequi.test.domain.model.entity.Product;
import com.seti.nequi.test.infrastructure.entrypoints.web.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public final class DtoMapper {

    private DtoMapper(){}

    public static Franchise toDomain(FranchiseDto dto) {
        return Franchise.builder()
                .id(dto.id())
                .name(dto.name())
                .branches(dto.branches() == null ? null :
                        dto.branches().stream().map(DtoMapper::toDomain).collect(Collectors.toList()))
                .build();
    }

    public static Branch toDomain(BranchDto dto) {
        return Branch.builder()
                .id(dto.id())
                .name(dto.name())
                .products(dto.products() == null ? null :
                        dto.products().stream().map(DtoMapper::toDomain).collect(Collectors.toList()))
                .build();
    }

    public static Product toDomain(ProductDto dto) {
        return Product.builder()
                .id(dto.id())
                .name(dto.name())
                .count(dto.count())
                .build();
    }

    public static FranchiseDto toDto(Franchise domain) {
        return new FranchiseDto(
                domain.getId(),
                domain.getName(),
                domain.getBranches() == null ? null :
                        domain.getBranches().stream().map(DtoMapper::toDto).collect(Collectors.toList())
        );
    }

    public static BranchDto toDto(Branch domain) {
        return new BranchDto(
                domain.getId(),
                domain.getName(),
                domain.getProducts() == null ? null :
                        domain.getProducts().stream().map(DtoMapper::toDto).collect(Collectors.toList())
        );
    }

    public static ProductDto toDto(Product domain) {
        return new ProductDto(
                domain.getId(),
                domain.getName(),
                domain.getCount()
        );
    }
}
