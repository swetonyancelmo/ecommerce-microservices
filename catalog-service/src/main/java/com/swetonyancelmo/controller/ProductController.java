package com.swetonyancelmo.controller;

import com.swetonyancelmo.dto.CreateProductDTO;
import com.swetonyancelmo.dto.ProductResponseDTO;
import com.swetonyancelmo.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product Controller", description = "API para gerenciamento de produtos")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController implements com.swetonyancelmo.controller.docs.ProductControllerDocs {

    private final ProductService service;

    @GetMapping
    @Override
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PostMapping
    @Override
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody CreateProductDTO dto) {
        return new ResponseEntity<>(service.createProduct(dto), HttpStatus.CREATED);
    }

}
