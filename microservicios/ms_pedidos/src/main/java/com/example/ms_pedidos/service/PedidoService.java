package com.example.ms_pedidos.service;

import java.util.List;

import com.example.ms_pedidos.dto.PedidoRequestDTO;
import com.example.ms_pedidos.dto.PedidoResponseDTO;

public interface PedidoService {

    PedidoResponseDTO crearPedido(PedidoRequestDTO request);
    PedidoResponseDTO obtenerPedido(Long id);
    List<PedidoResponseDTO> listarPedidos();
    void cancelarPedido(Long id);
     PedidoResponseDTO actualizarPedido(Long id, PedidoRequestDTO request); // <-- nuevo método
    void eliminarPedido(Long id); // <-- nuevo método

}
