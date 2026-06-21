package com.example.ms_pedidos.controller;

import com.example.ms_pedidos.assembler.PedidoModelAssembler;
import com.example.ms_pedidos.dto.PedidoRequestDTO;
import com.example.ms_pedidos.dto.PedidoResponseDTO;
import com.example.ms_pedidos.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PedidoControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private PedidoModelAssembler assembler;

    @InjectMocks
    private PedidoControllerV2 controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void debeCrearPedido() throws Exception {

        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setCliente("Ignacio");

        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setId(1L);
        response.setCliente("Ignacio");

        when(pedidoService.crearPedido(any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(post("/api/v2/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeObtenerPedido() throws Exception {

        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setId(1L);

        when(pedidoService.obtenerPedido(1L)).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/pedidos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeListarPedidos() throws Exception {

        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setId(1L);

        when(pedidoService.listarPedidos()).thenReturn(List.of(response));
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/pedidos"))
                .andExpect(status().isOk());
    }

    @Test
    void debeCancelarPedido() throws Exception {

        doNothing().when(pedidoService).cancelarPedido(1L);

        mockMvc.perform(put("/api/v2/pedidos/1/cancelar"))
                .andExpect(status().isNoContent());
    }
}