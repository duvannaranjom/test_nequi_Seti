package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountDTO {
    @Min(value = 0, message = "El stock no puede ser negativo")
    private int newCount;
}
