package com.example.ms_pedidos.service.impl;

import com.example.ms_pedidos.dto.PedidoRequestDTO;
import com.example.ms_pedidos.dto.PedidoResponseDTO;
import com.example.ms_pedidos.exception.ResourceNotFoundException;
import com.example.ms_pedidos.model.Pedido;
import com.example.ms_pedidos.repository.PedidoRepository;
import com.example.ms_pedidos.service.PedidoServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void debeCrearPedido() {

        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setCliente("Ignacio");

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PedidoResponseDTO response = pedidoService.crearPedido(request);

        assertEquals("Ignacio", response.getCliente());
        assertEquals("PENDIENTE", response.getEstado());
    }

    @Test
    void debeObtenerPedido() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoResponseDTO response = pedidoService.obtenerPedido(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void debeLanzarExcepcionSiNoExistePedido() {

        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> pedidoService.obtenerPedido(1L));
    }

    @Test
    void debeCancelarPedido() {

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado("PENDIENTE");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.cancelarPedido(1L);

        assertEquals("CANCELADO", pedido.getEstado());
        verify(pedidoRepository).save(pedido);
    }
}