package com.example.ms_pedidos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_pedidos.dto.DetallePedidoRequestDTO;
import com.example.ms_pedidos.dto.DetallePedidoResponseDTO;
import com.example.ms_pedidos.service.DetallePedidoService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/detalles")
@RequiredArgsConstructor
public class DetallePedidoController {

     private final DetallePedidoService detallePedidoService;

    @GetMapping
    public List<DetallePedidoResponseDTO> listarDetalles() {
        return detallePedidoService.listarDetalles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoResponseDTO> obtenerDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(detallePedidoService.obtenerDetalle(id));
    }

    @PostMapping
    public ResponseEntity<DetallePedidoResponseDTO> crearDetalle(@Valid @RequestBody DetallePedidoRequestDTO request) {
        return ResponseEntity.ok(detallePedidoService.crearDetalle(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedidoResponseDTO> actualizarDetalle(
            @PathVariable Long id,
            @Valid @RequestBody DetallePedidoRequestDTO request) {
        return ResponseEntity.ok(detallePedidoService.actualizarDetalle(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long id) {
        detallePedidoService.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }

}
