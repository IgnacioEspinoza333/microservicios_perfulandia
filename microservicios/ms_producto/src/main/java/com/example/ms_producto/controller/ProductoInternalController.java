package com.example.ms_producto.controller;

import com.example.ms_producto.dto.ProductoResumenDto;
import com.example.ms_producto.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoInternalController {

    private final ProductoService productoService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResumenDto> obtenerResumenPorId(@PathVariable Long id) {
        log.debug("Solicitud interna para obtener resumen de producto con id: {}", id);

        ProductoResumenDto dto = productoService.obtenerResumenPorId(id);
        return ResponseEntity.ok(dto);
    }
}