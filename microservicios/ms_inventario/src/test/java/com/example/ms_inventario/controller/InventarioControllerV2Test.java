package com.example.ms_inventario.controller;

import com.example.ms_inventario.assembler.InventarioModelAssembler;
import com.example.ms_inventario.dto.InventarioRequestDto;
import com.example.ms_inventario.dto.InventarioResponseDto;
import com.example.ms_inventario.dto.InventarioUpdateDto;
import com.example.ms_inventario.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class InventarioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioService inventarioService;

    @MockitoBean
    private InventarioModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private InventarioRequestDto requestDto;
    private InventarioUpdateDto updateDto;
    private InventarioResponseDto responseDto;
    private EntityModel<InventarioResponseDto> entityModel;

    @BeforeEach
    void setUp() {
        requestDto = new InventarioRequestDto();
        requestDto.setProductoId(1L);
        requestDto.setStock(10);

        updateDto = new InventarioUpdateDto();
        updateDto.setProductoId(1L);
        updateDto.setStock(20);

        responseDto = new InventarioResponseDto();
        responseDto.setId(1L);
        responseDto.setProductoId(1L);
        responseDto.setStock(10);
        responseDto.setVersion(0L);
        responseDto.setProducto(null);

        entityModel = EntityModel.of(
                responseDto,
                Link.of("http://localhost/api/v2/inventarios/1").withSelfRel(),
                Link.of("http://localhost/api/v2/inventarios").withRel("inventarios")
        );
    }

    @Test
    void testCreateInventario() throws Exception {
        when(inventarioService.crear(any(InventarioRequestDto.class)))
                .thenReturn(responseDto);

        when(assembler.toModel(responseDto))
                .thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productoId").value(1))
                .andExpect(jsonPath("$.stock").value(10));

        verify(inventarioService, times(1)).crear(any());
        verify(assembler, times(1)).toModel(responseDto);
    }

    @Test
    void testGetAllInventarios() throws Exception {
        when(inventarioService.listar())
                .thenReturn(List.of(responseDto));

        when(assembler.toModel(any(InventarioResponseDto.class)))
                .thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/inventarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(inventarioService, times(1)).listar();
        verify(assembler, times(1)).toModel(any(InventarioResponseDto.class));
    }

    @Test
    void testGetInventarioById() throws Exception {
        when(inventarioService.obtenerPorId(1L))
                .thenReturn(responseDto);

        when(assembler.toModel(responseDto))
                .thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/inventarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productoId").value(1))
                .andExpect(jsonPath("$.stock").value(10));

        verify(inventarioService, times(1)).obtenerPorId(1L);
        verify(assembler, times(1)).toModel(responseDto);
    }

    @Test
    void testGetInventarioByProductoId() throws Exception {
        when(inventarioService.obtenerPorProductoId(1L))
                .thenReturn(responseDto);

        when(assembler.toModel(responseDto))
                .thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/inventarios/producto/1"))
                .andExpect(status().isOk());

        verify(inventarioService, times(1)).obtenerPorProductoId(1L);
    }

    @Test
    void testUpdateInventario() throws Exception {
        InventarioResponseDto actualizado = new InventarioResponseDto();
        actualizado.setId(1L);
        actualizado.setProductoId(1L);
        actualizado.setStock(20);
        actualizado.setVersion(1L);
        actualizado.setProducto(null);

        EntityModel<InventarioResponseDto> updatedModel = EntityModel.of(actualizado);

        when(inventarioService.actualizar(eq(1L), any(InventarioUpdateDto.class)))
                .thenReturn(actualizado);

        when(assembler.toModel(actualizado))
                .thenReturn(updatedModel);

        mockMvc.perform(put("/api/v2/inventarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(20));

        verify(inventarioService, times(1))
                .actualizar(eq(1L), any(InventarioUpdateDto.class));
        verify(assembler, times(1)).toModel(actualizado);
    }

    @Test
    void testDeleteInventario() throws Exception {
        doNothing().when(inventarioService).eliminar(1L);

        mockMvc.perform(delete("/api/v2/inventarios/1"))
                .andExpect(status().isNoContent());

        verify(inventarioService, times(1)).eliminar(1L);
    }

    @Test
    void testCreateInventarioBadRequest() throws Exception {
        InventarioRequestDto invalido = new InventarioRequestDto();

        mockMvc.perform(post("/api/v2/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(inventarioService, never()).crear(any());
    }
}