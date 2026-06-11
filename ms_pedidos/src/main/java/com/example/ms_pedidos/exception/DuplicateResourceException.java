package com.example.ms_pedidos.exception;

public class DuplicateResourceException extends RuntimeException {
   public DuplicateResourceException(String message) {
        super(message);
    }
}
