package com.example.ms_proveedores.controller;

import com.example.ms_proveedores.dto.MessageResponseDto;
import com.example.ms_proveedores.dto.ProveedorRequestDto;
import com.example.ms_proveedores.dto.ProveedorResponseDto;
import com.example.ms_proveedores.dto.ProveedorUpdateDto;
import com.example.ms_proveedores.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Slf4j
public class ProveedorController {

    private final ProveedorService proveedorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProveedorResponseDto crear(@Valid @RequestBody ProveedorRequestDto dto) {
        log.info("Solicitud para crear proveedor con email: {}", dto.getEmail());
        return proveedorService.crear(dto);
    }

    @GetMapping
    public List<ProveedorResponseDto> listar() {
        log.debug("Solicitud para listar proveedores");
        return proveedorService.listar();
    }

    @GetMapping("/{id}")
    public ProveedorResponseDto obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud para obtener proveedor con id: {}", id);
        return proveedorService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public ProveedorResponseDto actualizar(@PathVariable Long id,
                                           @Valid @RequestBody ProveedorUpdateDto dto) {
        log.info("Solicitud para actualizar proveedor con id: {}", id);
        return proveedorService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        log.warn("Solicitud para eliminar proveedor con id: {}", id);
        return proveedorService.eliminar(id);
    }
}
