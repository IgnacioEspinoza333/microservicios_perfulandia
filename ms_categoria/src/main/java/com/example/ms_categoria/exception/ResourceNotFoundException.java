package com.example.ms_categoria.exception;

public class ResourceNotFoundException extends RuntimeException {
     public ResourceNotFoundException(String message) {
        super(message);
    }
}
