package com.swetonyancelmo.controller;

import com.swetonyancelmo.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping("/{productId}")
    public ResponseEntity<Boolean> isInStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        Boolean inStock = service.isInStock(productId, quantity);
        return ResponseEntity.ok(inStock);
    }

    @PostMapping("/{productId}/deduct")
    public ResponseEntity<String> deductStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        String result = service.deductStock(productId, quantity);
        return ResponseEntity.ok(result);
    }

}
