package com.example.ms_proveedores.controller;

import com.example.ms_proveedores.dto.AbastecimientoRequestDto;
import com.example.ms_proveedores.dto.AbastecimientoResponseDto;
import com.example.ms_proveedores.dto.AbastecimientoUpdateDto;
import com.example.ms_proveedores.dto.MessageResponseDto;
import com.example.ms_proveedores.service.AbastecimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AbastecimientoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AbastecimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AbastecimientoService abastecimientoService;

    @Autowired
    private ObjectMapper objectMapper;

    private AbastecimientoRequestDto requestDto;
    private AbastecimientoUpdateDto updateDto;
    private AbastecimientoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new AbastecimientoRequestDto();
        requestDto.setProveedorId(1L);
        requestDto.setProductoId(100L);
        requestDto.setCantidad(50);
        requestDto.setEstado("PENDIENTE");

        updateDto = new AbastecimientoUpdateDto();
        updateDto.setProveedorId(1L);
        updateDto.setNombreProveedor("Proveedor Uno Actualizado");
        updateDto.setProductoId(100L);
        updateDto.setCantidad(80);
        updateDto.setEstado("COMPLETADO");

        responseDto = new AbastecimientoResponseDto(
                1L,
                1L,
                "Proveedor Uno",
                100L,
                50,
                "PENDIENTE",
                Instant.now(),
                1L
        );
    }

    @Test
    public void testCreateAbastecimiento() throws Exception {
        when(abastecimientoService.crear(any(AbastecimientoRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/abastecimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.proveedorId").value(1))
                .andExpect(jsonPath("$.productoId").value(100))
                .andExpect(jsonPath("$.cantidad").value(50))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        verify(abastecimientoService, times(1)).crear(any(AbastecimientoRequestDto.class));
    }

    @Test
    public void testGetAllAbastecimientos() throws Exception {
        when(abastecimientoService.listar()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/abastecimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].proveedorId").value(1))
                .andExpect(jsonPath("$[0].productoId").value(100))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));

        verify(abastecimientoService, times(1)).listar();
    }

    @Test
    public void testGetAbastecimientoById() throws Exception {
        when(abastecimientoService.obtenerPorId(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/abastecimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.proveedorId").value(1))
                .andExpect(jsonPath("$.productoId").value(100))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        verify(abastecimientoService, times(1)).obtenerPorId(1L);
    }

    @Test
    public void testGetAbastecimientosByProveedor() throws Exception {
        when(abastecimientoService.listarPorProveedor(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/abastecimientos/proveedor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].proveedorId").value(1));

        verify(abastecimientoService, times(1)).listarPorProveedor(1L);
    }

    @Test
    public void testGetAbastecimientosByProducto() throws Exception {
        when(abastecimientoService.listarPorProducto(100L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/abastecimientos/producto/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(100));

        verify(abastecimientoService, times(1)).listarPorProducto(100L);
    }

    @Test
    public void testUpdateAbastecimiento() throws Exception {
        AbastecimientoResponseDto actualizado = new AbastecimientoResponseDto(
                1L,
                1L,
                "Proveedor Uno Actualizado",
                100L,
                80,
                "COMPLETADO",
                Instant.now(),
                1L
        );

        when(abastecimientoService.actualizar(eq(1L), any(AbastecimientoUpdateDto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/abastecimientos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(80))
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));

        verify(abastecimientoService, times(1)).actualizar(eq(1L), any(AbastecimientoUpdateDto.class));
    }

    @Test
    public void testDeleteAbastecimiento() throws Exception {
        when(abastecimientoService.eliminar(1L))
                .thenReturn(new MessageResponseDto("Abastecimiento eliminado correctamente"));

        mockMvc.perform(delete("/api/abastecimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Abastecimiento eliminado correctamente"));

        verify(abastecimientoService, times(1)).eliminar(1L);
    }
}