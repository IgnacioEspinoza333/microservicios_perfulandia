package com.example.ms_inventario.controller;

import com.example.ms_inventario.assembler.InventarioModelAssembler;
import com.example.ms_inventario.dto.InventarioRequestDto;
import com.example.ms_inventario.dto.InventarioResponseDto;
import com.example.ms_inventario.dto.InventarioUpdateDto;
import com.example.ms_inventario.service.InventarioService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InventarioControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private InventarioService inventarioService;

    @Mock
    private InventarioModelAssembler assembler;

    @InjectMocks
    private InventarioControllerV2 controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void debeCrearInventario() throws Exception {
        InventarioRequestDto request = new InventarioRequestDto();
        request.setProductoId(1L);
        request.setStock(10);

        InventarioResponseDto response =
                new InventarioResponseDto(1L, 1L, 10, 0L);

        when(inventarioService.crear(any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(post("/api/v2/inventarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeListarInventarios() throws Exception {
        InventarioResponseDto response =
                new InventarioResponseDto(1L, 1L, 10, 0L);

        when(inventarioService.listar()).thenReturn(List.of(response));
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/inventarios"))
                .andExpect(status().isOk());
    }

    @Test
    void debeObtenerPorId() throws Exception {
        InventarioResponseDto response =
                new InventarioResponseDto(1L, 1L, 10, 0L);

        when(inventarioService.obtenerPorId(1L)).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/inventarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeObtenerPorProductoId() throws Exception {
        InventarioResponseDto response =
                new InventarioResponseDto(1L, 1L, 10, 0L);

        when(inventarioService.obtenerPorProductoId(1L)).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/inventarios/producto/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeActualizarInventario() throws Exception {
        InventarioUpdateDto request = new InventarioUpdateDto();
        request.setProductoId(1L);
        request.setStock(20);

        InventarioResponseDto response =
                new InventarioResponseDto(1L, 1L, 20, 1L);

        when(inventarioService.actualizar(eq(1L), any()))
                .thenReturn(response);
        when(assembler.toModel(any()))
                .thenReturn(EntityModel.of(response));

        mockMvc.perform(put("/api/v2/inventarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarInventario() throws Exception {

        doNothing().when(inventarioService).eliminar(1L);

        mockMvc.perform(delete("/api/v2/inventarios/1"))
                .andExpect(status().isNoContent());

        verify(inventarioService).eliminar(1L);
    }
}
