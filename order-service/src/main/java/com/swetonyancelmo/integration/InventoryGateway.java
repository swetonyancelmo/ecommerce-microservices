package com.swetonyancelmo.integration;

import com.swetonyancelmo.exception.InventoryUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryGateway {

    private static final String INSTANCE = "inventoryService";

    private final InventoryClient inventoryClient;

    @CircuitBreaker(name = INSTANCE, fallbackMethod = "fallbackChecarEstoque")
    @Retry(name = INSTANCE)
    public Boolean checarEstoque(Long productId, Integer quantity) {
        return inventoryClient.isInStock(productId, quantity);
    }

    public Boolean fallbackChecarEstoque(Long productId, Integer quantity, Throwable t) {
        log.warn("Fallback ao checar estoque do produto {} (qtd {}): {}", productId, quantity, t.toString());
        throw new InventoryUnavailableException("Serviço de estoque indisponível ao checar disponibilidade", t);
    }

    @CircuitBreaker(name = INSTANCE, fallbackMethod = "fallbackDeduzirEstoque")
    @Retry(name = INSTANCE)
    public void deduzirEstoque(Long productId, Integer quantity) {
        inventoryClient.deductStock(productId, quantity);
    }

    public void fallbackDeduzirEstoque(Long productId, Integer quantity, Throwable t) {
        log.warn("Fallback ao deduzir estoque do produto {} (qtd {}): {}", productId, quantity, t.toString());
        throw new InventoryUnavailableException("Serviço de estoque indisponível ao deduzir estoque", t);
    }
}