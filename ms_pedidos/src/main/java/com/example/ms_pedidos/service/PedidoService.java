package com.example.ms_pedidos.service;

import java.util.List;

import com.example.ms_pedidos.dtos.PedidoRequestDTO;
import com.example.ms_pedidos.dtos.PedidoResponseDTO;

public interface PedidoService {

    PedidoResponseDTO crearPedido(PedidoRequestDTO request);
    PedidoResponseDTO obtenerPedido(Long id);
    List<PedidoResponseDTO> listarPedidos();
    void cancelarPedido(Long id);

}
