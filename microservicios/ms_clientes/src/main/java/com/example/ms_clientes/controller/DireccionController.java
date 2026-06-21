package com.example.ms_clientes.controller;

import com.example.ms_clientes.dto.DireccionRequestDto;
import com.example.ms_clientes.dto.DireccionResponseDto;
import com.example.ms_clientes.dto.DireccionUpdateDto;
import com.example.ms_clientes.dto.MessageResponseDto;
import com.example.ms_clientes.service.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DireccionController {

    private final DireccionService direccionService;

    @PostMapping("/api/clientes/{clienteId}/direcciones")
    @ResponseStatus(HttpStatus.CREATED)
    public DireccionResponseDto crear(@PathVariable Long clienteId,
                                      @Valid @RequestBody DireccionRequestDto dto) {
        log.info("Solicitud para crear dirección al cliente con id: {}", clienteId);
        return direccionService.crear(clienteId, dto);
    }

    @GetMapping("/api/direcciones")
    public List<DireccionResponseDto> listar() {
        log.debug("Solicitud para listar direcciones");
        return direccionService.listar();
    }

    @GetMapping("/api/clientes/{clienteId}/direcciones")
    public List<DireccionResponseDto> listarPorCliente(@PathVariable Long clienteId) {
        log.debug("Solicitud para listar direcciones del cliente con id: {}", clienteId);
        return direccionService.listarPorCliente(clienteId);
    }

    @GetMapping("/api/direcciones/{id}")
    public DireccionResponseDto obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud para obtener dirección con id: {}", id);
        return direccionService.obtenerPorId(id);
    }

    @PutMapping("/api/direcciones/{id}")
    public DireccionResponseDto actualizar(@PathVariable Long id,
                                           @Valid @RequestBody DireccionUpdateDto dto) {
        log.info("Solicitud para actualizar dirección con id: {}", id);
        return direccionService.actualizar(id, dto);
    }

    @DeleteMapping("/api/direcciones/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        log.warn("Solicitud para eliminar dirección con id: {}", id);
        return direccionService.eliminar(id);
    }
}