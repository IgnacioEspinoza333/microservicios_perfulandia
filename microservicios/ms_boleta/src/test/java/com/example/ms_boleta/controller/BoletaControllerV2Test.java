package com.example.ms_boleta.controller;

import com.example.ms_boleta.assembler.BoletaModelAssembler;
import com.example.ms_boleta.dto.BoletaRequestDTO;
import com.example.ms_boleta.dto.BoletaResponseDTO;
import com.example.ms_boleta.service.BoletaService;
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
class BoletaControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private BoletaService service;

    @Mock
    private BoletaModelAssembler assembler;

    @InjectMocks
    private BoletaControllerV2 controller;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
    }

    @Test
    void debeCrearBoleta() throws Exception {
        BoletaRequestDTO request = new BoletaRequestDTO();
        request.setNumero("B001");
        request.setCliente("Ignacio");
        request.setMonto(1000.0);

        BoletaResponseDTO response = BoletaResponseDTO.builder()
                .id(1L)
                .numero("B001")
                .cliente("Ignacio")
                .monto(1000.0)
                .fechaEmision(LocalDateTime.now())
                .build();

        when(service.crearBoleta(any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(post("/api/v2/boletas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeObtenerBoleta() throws Exception {
        BoletaResponseDTO response = new BoletaResponseDTO();

        when(service.obtenerBoleta(1L)).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/boletas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeListarBoletas() throws Exception {
        BoletaResponseDTO response = new BoletaResponseDTO();

        when(service.listarBoletas()).thenReturn(List.of(response));
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/boletas"))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarBoleta() throws Exception {
        doNothing().when(service).eliminarBoleta(1L);

        mockMvc.perform(delete("/api/v2/boletas/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminarBoleta(1L);
    }

    @Test
    void debeActualizarBoleta() throws Exception {
        BoletaRequestDTO request = new BoletaRequestDTO();
        request.setNumero("B002");
        request.setCliente("Nuevo");
        request.setMonto(2000.0);

        BoletaResponseDTO response = new BoletaResponseDTO();

        when(service.actualizarBoleta(eq(1L), any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(put("/api/v2/boletas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}