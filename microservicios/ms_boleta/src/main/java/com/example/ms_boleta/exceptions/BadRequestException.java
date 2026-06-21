package com.example.ms_boleta.exceptions;

public class BadRequestException extends RuntimeException {
     public BadRequestException(String message) {
        super(message);
    }
}
