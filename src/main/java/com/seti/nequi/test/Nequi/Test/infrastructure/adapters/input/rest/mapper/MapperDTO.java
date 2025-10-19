package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.mapper;

import com.seti.nequi.test.Nequi.Test.domain.model.Branch;
import com.seti.nequi.test.Nequi.Test.domain.model.Franchise;
import com.seti.nequi.test.Nequi.Test.domain.model.Product;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.dto.BranchDTO;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.dto.FranchiseDTO;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapperDTO {

    public Franchise toDomain(FranchiseDTO dto) {
        if (dto == null) {
            return null;
        }

        return Franchise.builder()
                .id(dto.getId())
                .name(dto.getName())
                .branches(dto.getBranches().stream()
                        .map(this::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public FranchiseDTO toDTO(Franchise domain) {
        if (domain == null) {
            return null;
        }

        return FranchiseDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .branches(domain.getBranches().stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public Branch toDomain(BranchDTO dto) {
        if (dto == null) {
            return null;
        }

        return Branch.builder()
                .id(dto.getId())
                .name(dto.getName())
                .products(dto.getProducts().stream()
                        .map(this::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public BranchDTO toDTO(Branch domain) {
        if (domain == null) {
            return null;
        }

        return BranchDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .products(domain.getProducts().stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public Product toDomain(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .count(dto.getStock())
                .build();
    }

    public ProductDTO toDTO(Product domain) {
        if (domain == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .stock(domain.getCount())
                .build();
    }
}
