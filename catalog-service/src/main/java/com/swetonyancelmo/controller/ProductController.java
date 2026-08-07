package com.swetonyancelmo.controller;

import com.swetonyancelmo.dto.CreateProductDTO;
import com.swetonyancelmo.dto.ErrorResponseDTO;
import com.swetonyancelmo.dto.ProductResponseDTO;
import com.swetonyancelmo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Operações de gerenciamento de produtos do catálogo")
public class ProductController {

    private final ProductService service;

    @GetMapping
    @Operation(summary = "Lista todos os produtos")
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PostMapping
    @Operation(summary = "Cria um novo produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody CreateProductDTO dto) {
        return new ResponseEntity<>(service.createProduct(dto), HttpStatus.CREATED);
    }

}
