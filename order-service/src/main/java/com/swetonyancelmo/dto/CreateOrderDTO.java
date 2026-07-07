package com.swetonyancelmo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record CreateOrderDTO(
        @NotEmpty(message = "O ID do produto é obrigatório")
        @Positive(message = "O ID do produto deve ser um número positivo")
        Long productId,

        @NotEmpty(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser um número positivo")
        Integer quantity
) {
}
