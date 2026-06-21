package com.example.ms_categoria.exception;

public class BadRequestException  extends RuntimeException{
     public BadRequestException(String message) {
        super(message);
    }
}
