package com.example.ms_inventario.controller;

import com.example.ms_inventario.dto.InventarioRequestDto;
import com.example.ms_inventario.dto.InventarioResponseDto;
import com.example.ms_inventario.dto.InventarioUpdateDto;
import com.example.ms_inventario.dto.MessageResponseDto;
import com.example.ms_inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
@Slf4j
public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventarioResponseDto crear(@Valid @RequestBody InventarioRequestDto dto) {
        log.info("Solicitud para crear inventario para productoId: {}", dto.getProductoId());
        return inventarioService.crear(dto);
    }

    @GetMapping
    public List<InventarioResponseDto> listar() {
        log.debug("Solicitud para listar inventarios");
        return inventarioService.listar();
    }

    @GetMapping("/{id}")
    public InventarioResponseDto obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud para obtener inventario con id: {}", id);
        return inventarioService.obtenerPorId(id);
    }

    @GetMapping("/producto/{productoId}")
    public InventarioResponseDto obtenerPorProductoId(@PathVariable Long productoId) {
        log.debug("Solicitud para obtener inventario por productoId: {}", productoId);
        return inventarioService.obtenerPorProductoId(productoId);
    }

    @PutMapping("/{id}")
    public InventarioResponseDto actualizar(@PathVariable Long id,
                                            @Valid @RequestBody InventarioUpdateDto dto) {
        log.info("Solicitud para actualizar inventario con id: {}", id);
        return inventarioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        log.warn("Solicitud para eliminar inventario con id: {}", id);
        return inventarioService.eliminar(id);
    }
}
