package com.swetonyancelmo.exception;

/**
 * Sinaliza que o inventory-service está indisponível (falha de infraestrutura),
 * distinguindo esse caso do fluxo de negócio "sem estoque".
 */
public class InventoryUnavailableException extends RuntimeException {

    public InventoryUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}