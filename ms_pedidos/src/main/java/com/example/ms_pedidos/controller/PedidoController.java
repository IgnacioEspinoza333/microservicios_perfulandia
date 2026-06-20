package com.example.ms_pedidos.controller;

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

import com.example.ms_pedidos.dto.PedidoRequestDTO;
import com.example.ms_pedidos.dto.PedidoResponseDTO;
import com.example.ms_pedidos.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Valid
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crear(@RequestBody PedidoRequestDTO request) {
        return ResponseEntity.ok(pedidoService.crearPedido(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }
       @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> actualizarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO request) {
        return ResponseEntity.ok(pedidoService.actualizarPedido(id, request));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
            pedidoService.eliminarPedido(id);
            return ResponseEntity.noContent().build();
}

    



}
