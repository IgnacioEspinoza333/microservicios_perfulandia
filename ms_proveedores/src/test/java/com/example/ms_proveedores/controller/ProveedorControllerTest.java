package com.example.ms_proveedores.controller;

import com.example.ms_proveedores.dto.MessageResponseDto;
import com.example.ms_proveedores.dto.ProveedorRequestDto;
import com.example.ms_proveedores.dto.ProveedorResponseDto;
import com.example.ms_proveedores.dto.ProveedorUpdateDto;
import com.example.ms_proveedores.service.ProveedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(ProveedorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProveedorService proveedorService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProveedorRequestDto requestDto;
    private ProveedorUpdateDto updateDto;
    private ProveedorResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new ProveedorRequestDto();
        requestDto.setNombre("Proveedor Uno");
        requestDto.setEmail("proveedor1@test.com");
        requestDto.setTelefono("987654321");
        requestDto.setDireccion("Calle 123");
        requestDto.setActivo(true);

        updateDto = new ProveedorUpdateDto();
        updateDto.setNombre("Proveedor Uno Actualizado");
        updateDto.setEmail("proveedor1updated@test.com");
        updateDto.setTelefono("999999999");
        updateDto.setDireccion("Nueva dirección 456");
        updateDto.setActivo(false);

        responseDto = new ProveedorResponseDto(
                1L,
                "Proveedor Uno",
                "proveedor1@test.com",
                "987654321",
                "Calle 123",
                true,
                1L
        );
    }

    @Test
    public void testCreateProveedor() throws Exception {
        when(proveedorService.crear(any(ProveedorRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Proveedor Uno"))
                .andExpect(jsonPath("$.email").value("proveedor1@test.com"));

        verify(proveedorService, times(1)).crear(any(ProveedorRequestDto.class));
    }

    @Test
    public void testGetAllProveedores() throws Exception {
        when(proveedorService.listar()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Proveedor Uno"))
                .andExpect(jsonPath("$[0].email").value("proveedor1@test.com"));

        verify(proveedorService, times(1)).listar();
    }

    @Test
    public void testGetProveedorById() throws Exception {
        when(proveedorService.obtenerPorId(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Proveedor Uno"))
                .andExpect(jsonPath("$.email").value("proveedor1@test.com"));

        verify(proveedorService, times(1)).obtenerPorId(1L);
    }

    @Test
    public void testUpdateProveedor() throws Exception {
        ProveedorResponseDto actualizado = new ProveedorResponseDto(
                1L,
                "Proveedor Uno Actualizado",
                "proveedor1updated@test.com",
                "999999999",
                "Nueva dirección 456",
                false,
                1L
        );

        when(proveedorService.actualizar(eq(1L), any(ProveedorUpdateDto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/proveedores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Proveedor Uno Actualizado"))
                .andExpect(jsonPath("$.email").value("proveedor1updated@test.com"))
                .andExpect(jsonPath("$.activo").value(false));

        verify(proveedorService, times(1)).actualizar(eq(1L), any(ProveedorUpdateDto.class));
    }

    @Test
    public void testDeleteProveedor() throws Exception {
        when(proveedorService.eliminar(1L))
                .thenReturn(new MessageResponseDto("Proveedor eliminado correctamente"));

        mockMvc.perform(delete("/api/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Proveedor eliminado correctamente"));

        verify(proveedorService, times(1)).eliminar(1L);
    }
}