package com.example.ms_envio.controller;

import com.example.ms_envio.assembler.EnvioModelAssembler;
import com.example.ms_envio.dto.EnvioRequestDTO;
import com.example.ms_envio.dto.EnvioResponseDTO;
import com.example.ms_envio.model.EstadoEnvio;
import com.example.ms_envio.service.EnvioService;
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
class EnvioControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private EnvioService envioService;

    @Mock
    private EnvioModelAssembler assembler;

    @InjectMocks
    private EnvioControllerV2 controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void debeCrearEnvio() throws Exception {
        EnvioRequestDTO request = new EnvioRequestDTO();
        request.setDireccionDestino("Calle 123");
        request.setCliente("Ignacio");
        request.setFechaEnvio(LocalDateTime.now());
        request.setEstado(EstadoEnvio.PENDIENTE);

        EnvioResponseDTO response = EnvioResponseDTO.builder()
                .id(1L)
                .direccionDestino("Calle 123")
                .cliente("Ignacio")
                .fechaEnvio(LocalDateTime.now())
                .estado(EstadoEnvio.PENDIENTE)
                .build();

        when(envioService.crearEnvio(any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(post("/api/v2/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeObtenerEnvio() throws Exception {
        EnvioResponseDTO response = EnvioResponseDTO.builder()
                .id(1L)
                .estado(EstadoEnvio.PENDIENTE)
                .build();

        when(envioService.obtenerEnvio(1L)).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/envios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeListarEnvios() throws Exception {
        EnvioResponseDTO response = EnvioResponseDTO.builder()
                .id(1L)
                .estado(EstadoEnvio.PENDIENTE)
                .build();

        when(envioService.listarEnvios()).thenReturn(List.of(response));
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/envios"))
                .andExpect(status().isOk());
    }

    @Test
    void debeCancelarEnvio() throws Exception {
        doNothing().when(envioService).cancelarEnvio(1L);

        mockMvc.perform(put("/api/v2/envios/1/cancelar"))
                .andExpect(status().isNoContent());

        verify(envioService).cancelarEnvio(1L);
    }

    @Test
    void debeActualizarEnvio() throws Exception {
        EnvioRequestDTO request = new EnvioRequestDTO();
        request.setId(1L);
        request.setDireccionDestino("Nueva dirección");
        request.setCliente("Nuevo cliente");
        request.setFechaEnvio(LocalDateTime.now());
        request.setEstado(EstadoEnvio.EN_CAMINO);

        EnvioResponseDTO response = EnvioResponseDTO.builder()
                .id(1L)
                .estado(EstadoEnvio.EN_CAMINO)
                .build();

        when(envioService.actualizarEnvio(eq(1L), any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(put("/api/v2/envios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}