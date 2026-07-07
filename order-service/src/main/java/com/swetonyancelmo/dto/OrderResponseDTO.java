package com.swetonyancelmo.dto;

public record OrderResponseDTO(
        Long id,
        Long productId,
        Integer quantity,
        String status
) {
}
