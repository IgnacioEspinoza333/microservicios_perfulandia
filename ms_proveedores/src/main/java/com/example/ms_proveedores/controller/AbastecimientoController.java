package com.example.ms_proveedores.controller;

import com.example.ms_proveedores.dto.AbastecimientoRequestDto;
import com.example.ms_proveedores.dto.AbastecimientoResponseDto;
import com.example.ms_proveedores.dto.AbastecimientoUpdateDto;
import com.example.ms_proveedores.dto.MessageResponseDto;
import com.example.ms_proveedores.service.AbastecimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abastecimientos")
@RequiredArgsConstructor
@Slf4j
public class AbastecimientoController {

    private final AbastecimientoService abastecimientoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AbastecimientoResponseDto crear(@Valid @RequestBody AbastecimientoRequestDto dto) {
        log.info("Solicitud para crear abastecimiento para proveedorId: {}", dto.getProveedorId());
        return abastecimientoService.crear(dto);
    }

    @GetMapping
    public List<AbastecimientoResponseDto> listar() {
        log.debug("Solicitud para listar abastecimientos");
        return abastecimientoService.listar();
    }

    @GetMapping("/{id}")
    public AbastecimientoResponseDto obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud para obtener abastecimiento con id: {}", id);
        return abastecimientoService.obtenerPorId(id);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public List<AbastecimientoResponseDto> listarPorProveedor(@PathVariable Long proveedorId) {
        log.debug("Solicitud para listar abastecimientos del proveedor con id: {}", proveedorId);
        return abastecimientoService.listarPorProveedor(proveedorId);
    }

    @GetMapping("/producto/{productoId}")
    public List<AbastecimientoResponseDto> listarPorProducto(@PathVariable Long productoId) {
        log.debug("Solicitud para listar abastecimientos del producto con id: {}", productoId);
        return abastecimientoService.listarPorProducto(productoId);
    }

    @PutMapping("/{id}")
    public AbastecimientoResponseDto actualizar(@PathVariable Long id,
                                                @Valid @RequestBody AbastecimientoUpdateDto dto) {
        log.info("Solicitud para actualizar abastecimiento con id: {}", id);
        return abastecimientoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        log.warn("Solicitud para eliminar abastecimiento con id: {}", id);
        return abastecimientoService.eliminar(id);
    }
}