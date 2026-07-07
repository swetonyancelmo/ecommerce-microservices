package com.swetonyancelmo.service;

import com.swetonyancelmo.dto.CreateOrderDTO;
import com.swetonyancelmo.dto.OrderResponseDTO;
import com.swetonyancelmo.integration.InventoryClient;
import com.swetonyancelmo.model.Order;
import com.swetonyancelmo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final InventoryClient inventoryClient;

    @Transactional
    public String placeOrder(CreateOrderDTO dto) {
        try {
            Boolean hasStock = inventoryClient.isInStock(dto.productId(), dto.quantity());

            if (hasStock != null && hasStock) {
                inventoryClient.deductStock(dto.productId(), dto.quantity());
                Order order = new Order();
                order.setProductId(dto.productId());
                order.setQuantity(dto.quantity());
                order.setStatus("COMPLETED");
                repository.save(order);

                return "Pedido criado com sucesso";
            } else {
                Order order = new Order();
                order.setProductId(dto.productId());
                order.setQuantity(dto.quantity());
                order.setStatus("FAILED_NO_STOCK");
                repository.save(order);
                return "Falha ao criar pedido: Estoque insuficiente";
            }
        } catch (Exception e) {
            return "Erro de comunicação com o serviço de estoque: " + e.getMessage();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return repository.findAll().stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        order.getProductId(),
                        order.getQuantity(),
                        order.getStatus()))
                .toList();
    }

}
