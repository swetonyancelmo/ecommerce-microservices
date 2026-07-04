package com.swetonyancelmo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record CreateProductDTO(

        @NotBlank(message = "O nome é obrigatório")
        @NotEmpty(message = "O nome não pode ser vazio")
        String name,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotBlank(message = "O preço é obrigatório")
        @DecimalMin(value = "0.00", inclusive = false, message = "O preço deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 dígitos decimais")
        BigDecimal price
) {
}
