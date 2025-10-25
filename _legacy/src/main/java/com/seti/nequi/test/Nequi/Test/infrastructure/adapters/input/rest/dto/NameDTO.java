package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameDTO {
    @NotBlank(message = "El nuevo nombre es obligatorio")
    private String newName;
}
