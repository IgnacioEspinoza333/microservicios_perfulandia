package com.example.ms_pedidos.service;

import java.util.List;

import com.example.ms_pedidos.dto.DetallePedidoRequestDTO;
import com.example.ms_pedidos.dto.DetallePedidoResponseDTO;

public interface DetallePedidoService {
    DetallePedidoResponseDTO crearDetalle(DetallePedidoRequestDTO request);

    DetallePedidoResponseDTO obtenerDetalle(Long id);

    List<DetallePedidoResponseDTO> listarDetalles();

    DetallePedidoResponseDTO actualizarDetalle(Long id, DetallePedidoRequestDTO request);

    void eliminarDetalle(Long id);

}
