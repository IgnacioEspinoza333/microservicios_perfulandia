package com.example.ms_producto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/api/public/ping")
    public String ping() {
        return "ms_producto operativo";
    }
}
/*
verificar que el microservicio está vivo
probar acceso público
servir como endpoint de test rápido
comprobar que SecurityConfig permite rutas públicas
*/