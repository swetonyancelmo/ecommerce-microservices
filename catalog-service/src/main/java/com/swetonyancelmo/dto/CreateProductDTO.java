package com.swetonyancelmo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductDTO(

        @Schema(description = "Nome do produto", example = "Teclado mecânico")
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @Schema(description = "Descrição do produto", example = "Teclado mecânico com switches azuis")
        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @Schema(description = "Preço do produto", example = "299.90")
        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.00", inclusive = false, message = "O preço deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 dígitos decimais")
        BigDecimal price
) {
}
