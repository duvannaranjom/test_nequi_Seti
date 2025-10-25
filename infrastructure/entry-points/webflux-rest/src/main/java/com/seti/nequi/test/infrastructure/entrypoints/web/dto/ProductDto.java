package com.seti.nequi.test.infrastructure.entrypoints.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductDto(
        @NotBlank(message = "id is required") String id,
        @NotBlank(message = "name is required") String name,
        @Min(value = 0, message = "count must be >= 0") int count
) {}
