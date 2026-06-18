package com.example.ms_producto.controller;

import com.example.ms_producto.dto.MessageResponseDto;
import com.example.ms_producto.dto.ProductoRequestDto;
import com.example.ms_producto.dto.ProductoResponseDto;
import com.example.ms_producto.dto.ProductoUpdateDto;
import com.example.ms_producto.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//hola funciona
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDto crear(@Valid @RequestBody ProductoRequestDto dto) {
        log.info("Solicitud para crear producto con SKU: {}", dto.getSku());
        return productoService.crear(dto);
    }

    @GetMapping
    public List<ProductoResponseDto> listar() {
        log.debug("Solicitud para listar productos");
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ProductoResponseDto obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud para obtener producto con id: {}", id);
        return productoService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public ProductoResponseDto actualizar(@PathVariable Long id,
                                          @Valid @RequestBody ProductoUpdateDto dto) {
        log.info("Solicitud para actualizar producto con id: {}", id);
        return productoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        log.warn("Solicitud para eliminar producto con id: {}", id);
        return productoService.eliminar(id);
    }
}