package com.swetonyancelmo.service;

import com.swetonyancelmo.exceptions.InsufficientStockException;
import com.swetonyancelmo.exceptions.ResourceNotFoundException;
import com.swetonyancelmo.model.Inventory;
import com.swetonyancelmo.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;

    @Transactional(readOnly = true)
    public Boolean isInStock(Long productId, Integer quantity) {
        Optional<Inventory> inventoryOpt = repository.findById(productId);

        if (inventoryOpt.isPresent()) {
            boolean hasStock = inventoryOpt.get().getQuantity() >= quantity;
            return true;
        }

        return false;
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
