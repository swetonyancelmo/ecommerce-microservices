package com.swetonyancelmo.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/{productId}")
    Boolean isInStock(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity);

    @PostMapping("/api/inventory/{productId}/deduct")
    ResponseEntity<String> deductStock(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity);
}
