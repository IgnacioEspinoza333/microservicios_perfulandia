package com.example.ms_pago.controller;

import com.example.ms_pago.assembler.PagoModelAssembler;
import com.example.ms_pago.dto.PagoRequestDTO;
import com.example.ms_pago.dto.PagoResponseDTO;
import com.example.ms_pago.service.PagoService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PagoControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private PagoService pagoService;

    @Mock
    private PagoModelAssembler assembler;

    @InjectMocks
    private PagoControllerV2 controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void debeCrearPago() throws Exception {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setMonto(100.0);
        request.setMetodo("Tarjeta");

        PagoResponseDTO response = PagoResponseDTO.builder()
                .id(1L)
                .monto(100.0)
                .metodo("Tarjeta")
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();

        when(pagoService.crearPago(any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(post("/api/v2/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeObtenerPago() throws Exception {
        PagoResponseDTO response = PagoResponseDTO.builder()
                .id(1L)
                .monto(100.0)
                .metodo("Tarjeta")
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();

        when(pagoService.obtenerPago(1L)).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/pagos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeListarPagos() throws Exception {
        PagoResponseDTO response = PagoResponseDTO.builder()
                .id(1L)
                .monto(100.0)
                .metodo("Tarjeta")
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();

        when(pagoService.listarPagos()).thenReturn(List.of(response));
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/pagos"))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarPago() throws Exception {
        doNothing().when(pagoService).eliminarPago(1L);

        mockMvc.perform(delete("/api/v2/pagos/1"))
                .andExpect(status().isNoContent());

        verify(pagoService).eliminarPago(1L);
    }
}