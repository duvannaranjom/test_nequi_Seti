package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int stock;
}
