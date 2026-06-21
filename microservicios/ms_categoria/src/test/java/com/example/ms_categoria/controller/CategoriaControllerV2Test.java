package com.example.ms_categoria.controller;

import com.example.ms_categoria.assembler.CategoriaModelAssembler;
import com.example.ms_categoria.dto.CategoriaRequestDTO;
import com.example.ms_categoria.dto.CategoriaResponseDTO;
import com.example.ms_categoria.service.CategoriaService;
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
class CategoriaControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private CategoriaService service;

    @Mock
    private CategoriaModelAssembler assembler;

    @InjectMocks
    private CategoriaControllerV2 controller;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
    }

    @Test
    void debeCrearCategoria() throws Exception {
        CategoriaRequestDTO request = CategoriaRequestDTO.builder()
                .nombre("Electrónica")
                .descripcion("Productos electrónicos")
                .build();

        CategoriaResponseDTO response = new CategoriaResponseDTO(1L, "Electrónica", "Productos electrónicos");

        when(service.crearCategoria(any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(post("/api/v2/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeObtenerCategoria() throws Exception {
        CategoriaResponseDTO response = new CategoriaResponseDTO(1L, "Electrónica", "Desc");

        when(service.obtenerCategoria(1L)).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/categorias/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeListarCategorias() throws Exception {
        CategoriaResponseDTO response = new CategoriaResponseDTO(1L, "Electrónica", "Desc");

        when(service.listarCategorias()).thenReturn(List.of(response));
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(get("/api/v2/categorias"))
                .andExpect(status().isOk());
    }

    @Test
    void debeActualizarCategoria() throws Exception {
        CategoriaRequestDTO request = CategoriaRequestDTO.builder()
                .nombre("Nueva")
                .descripcion("Actualizada")
                .build();

        CategoriaResponseDTO response = new CategoriaResponseDTO(1L, "Nueva", "Actualizada");

        when(service.actualizarCategoria(eq(1L), any())).thenReturn(response);
        when(assembler.toModel(any())).thenReturn(EntityModel.of(response));

        mockMvc.perform(put("/api/v2/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarCategoria() throws Exception {
        doNothing().when(service).eliminarCategoria(1L);

        mockMvc.perform(delete("/api/v2/categorias/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminarCategoria(1L);
    }
}