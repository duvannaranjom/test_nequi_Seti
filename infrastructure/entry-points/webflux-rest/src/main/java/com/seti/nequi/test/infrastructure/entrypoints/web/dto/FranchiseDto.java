package com.seti.nequi.test.infrastructure.entrypoints.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record   FranchiseDto(
        String id, // opcional en creacion
        @NotBlank(message = "name is required") String name,
        @Valid List<BranchDto> branches
) {}
