package com.swetonyancelmo.controller;

import com.swetonyancelmo.dto.CreateProductDTO;
import com.swetonyancelmo.dto.ProductResponseDTO;
import com.swetonyancelmo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody CreateProductDTO dto) {
        return new ResponseEntity<>(service.createProduct(dto), HttpStatus.CREATED);
    }

}
