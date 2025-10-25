package com.seti.nequi.test.infrastructure.entrypoints.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record BranchDto(
        @NotBlank(message = "id is required") String id,
        @NotBlank(message = "name is required") String name,
        @Valid List<ProductDto> products
) {}
