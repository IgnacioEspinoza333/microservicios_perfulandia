package com.example.ms_boleta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_boleta.dto.BoletaRequestDTO;
import com.example.ms_boleta.dto.BoletaResponseDTO;
import com.example.ms_boleta.service.BoletaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boletas")
@RequiredArgsConstructor
public class BoletaController {
      private final BoletaService boletaService;

    @PostMapping
    public ResponseEntity<BoletaResponseDTO> crear(@RequestBody BoletaRequestDTO request) {
        return ResponseEntity.ok(boletaService.crearBoleta(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(boletaService.obtenerBoleta(id));
    }

    @GetMapping
    public ResponseEntity<List<BoletaResponseDTO>> listar() {
        return ResponseEntity.ok(boletaService.listarBoletas());
    }

     @PutMapping("/{id}")
    public ResponseEntity<BoletaResponseDTO> actualizarBoleta(
            @PathVariable Long id,
            @Valid @RequestBody BoletaRequestDTO request) {
        return ResponseEntity.ok(boletaService.actualizarBoleta(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletaService.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }
}
