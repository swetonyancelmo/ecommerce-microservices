package com.swetonyancelmo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderDTO(
        @NotNull(message = "O ID do produto é obrigatório")
        @Positive(message = "O ID do produto deve ser um número positivo")
        Long productId,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser um número positivo")
        Integer quantity
) {
}
