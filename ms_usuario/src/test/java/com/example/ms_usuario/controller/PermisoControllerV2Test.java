package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.PermisoModelAssembler;
import com.example.ms_usuario.dto.PermisoRequestDto;
import com.example.ms_usuario.dto.PermisoResponseDto;
import com.example.ms_usuario.service.PermisoService;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PermisoControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class PermisoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PermisoService permisoService;

    @MockitoBean
    private PermisoModelAssembler assembler;

    private PermisoRequestDto requestDto;
    private PermisoResponseDto responseDto;
    private EntityModel<PermisoResponseDto> entityModel;

    @BeforeEach
    void setUp() {
        requestDto = new PermisoRequestDto();
        requestDto.setCodigo("PERM_ADMIN");

        responseDto = new PermisoResponseDto(1L, "PERM_ADMIN");

        entityModel = EntityModel.of(
                responseDto,
                Link.of("http://localhost/api/v2/permisos/1").withSelfRel(),
                Link.of("http://localhost/api/v2/permisos").withRel("permisos")
        );
    }

    @Test
    void listar_deberiaRetornar200() throws Exception {
        when(permisoService.listar()).thenReturn(List.of(responseDto));
        when(assembler.toModel(any(PermisoResponseDto.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/permisos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(permisoService).listar();
        verify(assembler).toModel(any(PermisoResponseDto.class));
    }

    @Test
    void obtenerPorId_deberiaRetornar200() throws Exception {
        when(permisoService.obtenerPorId(1L)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/permisos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("PERM_ADMIN"));

        verify(permisoService).obtenerPorId(1L);
    }

    @Test
    void crear_deberiaRetornar201() throws Exception {
        when(permisoService.crear(any(PermisoRequestDto.class))).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/permisos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.codigo").value("PERM_ADMIN"));

        verify(permisoService).crear(any(PermisoRequestDto.class));
    }

    @Test
    void actualizar_deberiaRetornar200() throws Exception {
        PermisoRequestDto update = new PermisoRequestDto();
        update.setCodigo("PERM_UPDATED");

        PermisoResponseDto updated = new PermisoResponseDto(1L, "PERM_UPDATED");

        EntityModel<PermisoResponseDto> updatedModel = EntityModel.of(updated);

        when(permisoService.actualizar(eq(1L), any(PermisoRequestDto.class))).thenReturn(updated);
        when(assembler.toModel(updated)).thenReturn(updatedModel);

        mockMvc.perform(put("/api/v2/permisos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("PERM_UPDATED"));

        verify(permisoService).actualizar(eq(1L), any(PermisoRequestDto.class));
    }

    @Test
    void eliminar_deberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/api/v2/permisos/1"))
                .andExpect(status().isNoContent());

        verify(permisoService).eliminar(1L);
    }

    @Test
    void crear_deberiaRetornar400SiBodyInvalido() throws Exception {
        PermisoRequestDto invalido = new PermisoRequestDto();

        mockMvc.perform(post("/api/v2/permisos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(permisoService, never()).crear(any());
    }
}