package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.RolModelAssembler;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.RolRequestDto;
import com.example.ms_usuario.dto.RolResponseDto;
import com.example.ms_usuario.service.RolService;
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

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
public class RolControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RolService rolService;

    @MockitoBean
    private RolModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private RolRequestDto requestDto;
    private RolResponseDto responseDto;
    private EntityModel<RolResponseDto> entityModel;

    @BeforeEach
    void setUp() {
        requestDto = new RolRequestDto();
        requestDto.setNombre("ADMIN");
        requestDto.setPermisoIds(Set.of());

        responseDto = new RolResponseDto(1L, "ADMIN", Set.of());

        entityModel = EntityModel.of(
                responseDto,
                Link.of("http://localhost/api/v2/roles/1").withSelfRel(),
                Link.of("http://localhost/api/v2/roles").withRel("roles")
        );
    }

    @Test
    public void testGetAllRoles() throws Exception {
        when(rolService.listar()).thenReturn(java.util.List.of(responseDto));
        when(assembler.toModel(any(RolResponseDto.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(rolService, times(1)).listar();
    }

    @Test
    public void testGetRolById() throws Exception {
        when(rolService.obtenerPorId(1L)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ADMIN"));

        verify(rolService, times(1)).obtenerPorId(1L);
    }

    @Test
    public void testCreateRol() throws Exception {
        when(rolService.crear(any(RolRequestDto.class))).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ADMIN"));

        verify(rolService, times(1)).crear(any(RolRequestDto.class));
    }

    @Test
    public void testUpdateRol() throws Exception {
        RolResponseDto actualizado = new RolResponseDto(1L, "USER", Set.of());
        EntityModel<RolResponseDto> updatedModel = EntityModel.of(actualizado);

        requestDto.setNombre("USER");

        when(rolService.actualizar(eq(1L), any(RolRequestDto.class))).thenReturn(actualizado);
        when(assembler.toModel(actualizado)).thenReturn(updatedModel);

        mockMvc.perform(put("/api/v2/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("USER"));

        verify(rolService, times(1)).actualizar(eq(1L), any(RolRequestDto.class));
    }

    @Test
    public void testDeleteRol() throws Exception {
        when(rolService.eliminar(1L))
                .thenReturn(new MessageResponseDto("Rol eliminado correctamente"));

        mockMvc.perform(delete("/api/v2/roles/1"))
                .andExpect(status().isNoContent());

        verify(rolService, times(1)).eliminar(1L);
    }

    @Test
    public void testCreateRolBadRequest() throws Exception {
        RolRequestDto invalido = new RolRequestDto();

        mockMvc.perform(post("/api/v2/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(rolService, never()).crear(any());
    }
}