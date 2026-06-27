package com.swetonyancelmo.controller;

import com.swetonyancelmo.integration.InventoryClient;
import com.swetonyancelmo.model.Order;
import com.swetonyancelmo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository repository;
    private final InventoryClient inventoryClient;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody Order orderRequest) {
        try {
            Boolean hasStock = inventoryClient.isInStock(orderRequest.getProductId(), orderRequest.getQuantity());

            if (hasStock != null && hasStock) {
                inventoryClient.deductStock(orderRequest.getProductId(), orderRequest.getQuantity());
                orderRequest.setStatus("COMPLETED");
                repository.save(orderRequest);

                return ResponseEntity.ok("Pedido criado com sucesso");
            } else {
                orderRequest.setStatus("FAILED_NO_STOCK");
                repository.save(orderRequest);
                return ResponseEntity.badRequest().body("Falha ao criar pedido: Estoque insuficiente");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro de comunicação com o serviço de estoque: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return repository.findAll();
    }
}
