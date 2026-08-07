package com.swetonyancelmo.controller;

import com.swetonyancelmo.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Operações de gerenciamento do estoque dos produtos")
public class InventoryController {

    private final InventoryService service;

    @GetMapping("/{productId}")
    @Operation(summary = "Verifica se tem a quantidade solicitada no estoque")
    public ResponseEntity<Boolean> isInStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        Boolean inStock = service.isInStock(productId, quantity);
        return ResponseEntity.ok(inStock);
    }

    @PostMapping("/{productId}/deduct")
    @Operation(summary = "Retira produto do estoque")
    public ResponseEntity<String> deductStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        String result = service.deductStock(productId, quantity);
        return ResponseEntity.ok(result);
    }

}
