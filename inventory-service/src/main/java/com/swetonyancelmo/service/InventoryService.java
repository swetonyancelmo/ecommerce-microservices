package com.swetonyancelmo.service;

import com.swetonyancelmo.exceptions.InsufficientStockException;
import com.swetonyancelmo.exceptions.ResourceNotFoundException;
import com.swetonyancelmo.model.Inventory;
import com.swetonyancelmo.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;

    @Transactional(readOnly = true)
    public Boolean isInStock(Long productId, Integer quantity) {
        return repository.findByProductId(productId)
                .map(inventory -> inventory.getQuantity() >= quantity)
                .orElse(false);
    }

    @Transactional
    public String deductStock(Long productId, Integer quantity) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado para o produto " + productId));

        if (inventory.getQuantity() < quantity) {
            return new InsufficientStockException("Estoque insuficiente para o produto " + productId).getMessage();
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        repository.save(inventory);

        return "Estoque reduzido com sucesso!";
    }

}
