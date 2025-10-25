package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private String id;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String name;

    @Builder.Default
    private List<ProductDTO> products = new ArrayList<>();
}
