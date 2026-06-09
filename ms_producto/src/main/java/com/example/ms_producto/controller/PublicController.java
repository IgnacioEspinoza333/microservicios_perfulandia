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