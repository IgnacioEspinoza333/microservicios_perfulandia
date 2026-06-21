package com.example.ms_clientes.controller;

import com.example.ms_clientes.assembler.DireccionModelAssembler;
import com.example.ms_clientes.dto.DireccionRequestDto;
import com.example.ms_clientes.dto.DireccionResponseDto;
import com.example.ms_clientes.service.DireccionService;
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
class DireccionControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private DireccionService service;

    @Mock
    private DireccionModelAssembler assembler;

    @InjectMocks
    private DireccionControllerV2 controller;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
    }

    @Test
    void debeCrearDireccion() throws Exception {
        DireccionRequestDto dto = new DireccionRequestDto();
        dto.setCalle("Calle 1");

        DireccionResponseDto res = new DireccionResponseDto();
        res.setId(1L);

        when(service.crear(eq(1L), any())).thenReturn(res);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(res));

        mockMvc.perform(post("/api/v2/clientes/1/direcciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeListarDirecciones() throws Exception {
        DireccionResponseDto res = new DireccionResponseDto();
        res.setId(1L);

        when(service.listar()).thenReturn(List.of(res));
        when(assembler.toModel(any())).thenReturn(EntityModel.of(res));

        mockMvc.perform(get("/api/v2/direcciones"))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarDireccion() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v2/direcciones/1"))
                .andExpect(status().isNoContent());
    }
}