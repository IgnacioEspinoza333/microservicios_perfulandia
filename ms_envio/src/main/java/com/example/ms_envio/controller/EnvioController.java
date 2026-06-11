package com.example.ms_envio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_envio.dto.EnvioRequestDTO;
import com.example.ms_envio.dto.EnvioResponseDTO;
import com.example.ms_envio.service.EnvioService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {
        private final EnvioService envioService;

    @PostMapping
    public ResponseEntity<EnvioResponseDTO> crear(@RequestBody EnvioRequestDTO request) {
        return ResponseEntity.ok(envioService.crearEnvio(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerEnvio(id));
    }

    @GetMapping
    public ResponseEntity<List<EnvioResponseDTO>> listar() {
        return ResponseEntity.ok(envioService.listarEnvios());
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        envioService.cancelarEnvio(id);
        return ResponseEntity.noContent().build();
    }

}
