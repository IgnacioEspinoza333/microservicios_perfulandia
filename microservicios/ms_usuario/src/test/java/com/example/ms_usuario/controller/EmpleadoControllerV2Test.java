package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.EmpleadoModelAssembler;
import com.example.ms_usuario.dto.EmpleadoRequestDto;
import com.example.ms_usuario.dto.EmpleadoResponseDto;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.service.EmpleadoService;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class EmpleadoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmpleadoService empleadoService;

    @MockitoBean
    private EmpleadoModelAssembler assembler;

    private EmpleadoRequestDto requestDto;
    private EmpleadoResponseDto responseDto;
    private EntityModel<EmpleadoResponseDto> entityModel;

    @BeforeEach
    void setUp() {
        requestDto = new EmpleadoRequestDto();
        requestDto.setUsuarioId(10L);
        requestDto.setActivo(true);

        responseDto = new EmpleadoResponseDto(
                1L,
                10L,
                "Ignacio",
                "ignacio@test.com",
                true
        );

        entityModel = EntityModel.of(
                responseDto,
                Link.of("http://localhost/api/v2/empleados/1").withSelfRel(),
                Link.of("http://localhost/api/v2/empleados").withRel("empleados")
        );
    }

    @Test
    void listar_deberiaRetornar200() throws Exception {
        when(empleadoService.listar()).thenReturn(List.of(responseDto));
        when(assembler.toModel(any(EmpleadoResponseDto.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/empleados"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(empleadoService).listar();
        verify(assembler, times(1)).toModel(any(EmpleadoResponseDto.class));
    }

    @Test
    void obtenerPorId_deberiaRetornar200YEmpleado() throws Exception {
        when(empleadoService.obtenerPorId(1L)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value(10))
                .andExpect(jsonPath("$.nombreUsuario").value("Ignacio"))
                .andExpect(jsonPath("$.emailUsuario").value("ignacio@test.com"))
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(empleadoService).obtenerPorId(1L);
        verify(assembler).toModel(responseDto);
    }

    @Test
    void crear_deberiaRetornar201() throws Exception {
        when(empleadoService.crear(any(EmpleadoRequestDto.class))).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value(10))
                .andExpect(jsonPath("$.nombreUsuario").value("Ignacio"))
                .andExpect(jsonPath("$.emailUsuario").value("ignacio@test.com"))
                .andExpect(jsonPath("$.activo").value(true));

        verify(empleadoService).crear(any(EmpleadoRequestDto.class));
        verify(assembler).toModel(responseDto);
    }

    @Test
    void crear_deberiaRetornar400CuandoFaltaUsuarioId() throws Exception {
        EmpleadoRequestDto invalido = new EmpleadoRequestDto();
        invalido.setActivo(true);

        mockMvc.perform(post("/api/v2/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(empleadoService, never()).crear(any());
    }

    @Test
    void crear_deberiaRetornar400CuandoFaltaActivo() throws Exception {
        EmpleadoRequestDto invalido = new EmpleadoRequestDto();
        invalido.setUsuarioId(10L);

        mockMvc.perform(post("/api/v2/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(empleadoService, never()).crear(any());
    }

    @Test
    void actualizar_deberiaRetornar200() throws Exception {
        EmpleadoResponseDto actualizado = new EmpleadoResponseDto(
                1L,
                10L,
                "Ignacio",
                "ignacio@test.com",
                false
        );

        EntityModel<EmpleadoResponseDto> updatedModel = EntityModel.of(
                actualizado,
                Link.of("http://localhost/api/v2/empleados/1").withSelfRel(),
                Link.of("http://localhost/api/v2/empleados").withRel("empleados")
        );

        requestDto.setActivo(false);

        when(empleadoService.actualizar(eq(1L), any(EmpleadoRequestDto.class))).thenReturn(actualizado);
        when(assembler.toModel(actualizado)).thenReturn(updatedModel);

        mockMvc.perform(put("/api/v2/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuarioId").value(10))
                .andExpect(jsonPath("$.activo").value(false));

        verify(empleadoService).actualizar(eq(1L), any(EmpleadoRequestDto.class));
        verify(assembler).toModel(actualizado);
    }

    @Test
    void eliminar_deberiaRetornar204() throws Exception {
        when(empleadoService.eliminar(1L))
                .thenReturn(new MessageResponseDto("Empleado eliminado correctamente"));

        mockMvc.perform(delete("/api/v2/empleados/1"))
                .andExpect(status().isNoContent());

        verify(empleadoService).eliminar(1L);
    }
}