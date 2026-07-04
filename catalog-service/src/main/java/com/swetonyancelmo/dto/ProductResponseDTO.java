package com.swetonyancelmo.dto;

import java.math.BigDecimal;

public record ProductResponseDTO (
        String name,
        String description,
        BigDecimal price
) {
}
