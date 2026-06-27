package com.swetonyancelmo.controller;

import com.swetonyancelmo.model.Inventory;
import com.swetonyancelmo.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository repository;

    @GetMapping("/{productId}")
    public ResponseEntity<Boolean> isInStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        Optional<Inventory> inventoryOpt = repository.findByProductId(productId);

        if (inventoryOpt.isPresent()) {
            boolean hasStock = inventoryOpt.get().getQuantity() >= quantity;
            return ResponseEntity.ok(hasStock);
        }

        return ResponseEntity.ok(false);
    }

    @PostMapping("/{productId}/deduct")
    public ResponseEntity<String> deductStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o produto " + productId));

        if (inventory.getQuantity() < quantity) {
            return ResponseEntity.badRequest().body("Estoque insuficiente");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        repository.save(inventory);

        return ResponseEntity.ok("Estoque reduzido com sucesso");
    }

}
