package com.swetonyancelmo.controller;

import com.swetonyancelmo.dto.CreateOrderDTO;
import com.swetonyancelmo.dto.OrderResponseDTO;
import com.swetonyancelmo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Operações de pedidos de produtos")
public class OrderController {

    private final OrderService service;

    @PostMapping
    @Operation(summary = "Realiza um pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido realizado"),
            @ApiResponse(responseCode = "400", description = "Erro ao realizar o pedido")
    })
    public ResponseEntity<String> placeOrder(@Valid @RequestBody CreateOrderDTO dto) {
        String responseMessage = service.placeOrder(dto);
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping
    @Operation(summary = "Lista todos os pedidos")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }
}
