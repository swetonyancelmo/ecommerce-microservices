package com.swetonyancelmo.controller;

import com.swetonyancelmo.dto.CreateOrderDTO;
import com.swetonyancelmo.dto.OrderResponseDTO;
import com.swetonyancelmo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody CreateOrderDTO dto) {
        String responseMessage = service.placeOrder(dto);
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }
}
