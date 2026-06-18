package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.UsuarioModelAssembler;
import com.example.ms_usuario.dto.AsignarRolRequestDto;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.UsuarioRequestDto;
import com.example.ms_usuario.dto.UsuarioResponseDto;
import com.example.ms_usuario.dto.UsuarioUpdateDto;
import com.example.ms_usuario.service.UsuarioService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioModelAssembler assembler;

    private UsuarioRequestDto requestDto;
    private UsuarioUpdateDto updateDto;
    private UsuarioResponseDto responseDto;
    private EntityModel<UsuarioResponseDto> entityModel;
    private AsignarRolRequestDto asignarRolRequestDto;

    @BeforeEach
    void setUp() {
        requestDto = new UsuarioRequestDto();
        requestDto.setNombre("Ignacio");
        requestDto.setEmail("ignacio@test.com");
        requestDto.setPassword("123456");
        requestDto.setActivo(true);

        updateDto = new UsuarioUpdateDto();
        updateDto.setNombre("Ignacio Actualizado");
        updateDto.setEmail("ignacio.actualizado@test.com");
        updateDto.setPassword("nuevaPassword");
        updateDto.setActivo(false);

        responseDto = new UsuarioResponseDto(
                1L,
                "Ignacio",
                "ignacio@test.com",
                true,
                List.of("ADMIN"),
                false
        );

        entityModel = EntityModel.of(
                responseDto,
                Link.of("http://localhost/api/v2/usuarios/1").withSelfRel(),
                Link.of("http://localhost/api/v2/usuarios").withRel("usuarios"),
                Link.of("http://localhost/api/v2/usuarios/1/roles").withRel("roles")
        );

        asignarRolRequestDto = new AsignarRolRequestDto();
        asignarRolRequestDto.setRolId(2L);
    }

    @Test
    void listar_deberiaRetornar200() throws Exception {
        when(usuarioService.listar()).thenReturn(List.of(responseDto));
        when(assembler.toModel(any(UsuarioResponseDto.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(usuarioService).listar();
        verify(assembler).toModel(any(UsuarioResponseDto.class));
    }

    @Test
    void obtenerPorId_deberiaRetornar200YUsuario() throws Exception {
        when(usuarioService.obtenerPorId(1L)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ignacio"))
                .andExpect(jsonPath("$.email").value("ignacio@test.com"))
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$.roles[0]").value("ADMIN"))
                .andExpect(jsonPath("$.esEmpleado").value(false))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(usuarioService).obtenerPorId(1L);
        verify(assembler).toModel(responseDto);
    }

    @Test
    void crear_deberiaRetornar201() throws Exception {
        when(usuarioService.crear(any(UsuarioRequestDto.class))).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ignacio"))
                .andExpect(jsonPath("$.email").value("ignacio@test.com"));

        verify(usuarioService).crear(any(UsuarioRequestDto.class));
        verify(assembler).toModel(responseDto);
    }

    @Test
    void actualizar_deberiaRetornar200() throws Exception {
        UsuarioResponseDto actualizado = new UsuarioResponseDto(
                1L,
                "Ignacio Actualizado",
                "ignacio.actualizado@test.com",
                false,
                List.of("ADMIN"),
                false
        );

        EntityModel<UsuarioResponseDto> updatedModel = EntityModel.of(
                actualizado,
                Link.of("http://localhost/api/v2/usuarios/1").withSelfRel(),
                Link.of("http://localhost/api/v2/usuarios").withRel("usuarios"),
                Link.of("http://localhost/api/v2/usuarios/1/roles").withRel("roles")
        );

        when(usuarioService.actualizar(eq(1L), any(UsuarioUpdateDto.class))).thenReturn(actualizado);
        when(assembler.toModel(actualizado)).thenReturn(updatedModel);

        mockMvc.perform(put("/api/v2/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ignacio Actualizado"))
                .andExpect(jsonPath("$.email").value("ignacio.actualizado@test.com"))
                .andExpect(jsonPath("$.activo").value(false));

        verify(usuarioService).actualizar(eq(1L), any(UsuarioUpdateDto.class));
        verify(assembler).toModel(actualizado);
    }

    @Test
    void eliminar_deberiaRetornar204() throws Exception {
        when(usuarioService.eliminar(1L))
                .thenReturn(new MessageResponseDto("Usuario eliminado correctamente"));

        mockMvc.perform(delete("/api/v2/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminar(1L);
    }

    @Test
    void asignarRol_deberiaRetornar200() throws Exception {
        MessageResponseDto response = new MessageResponseDto("Rol asignado correctamente al usuario");

        when(usuarioService.asignarRol(eq(1L), any(AsignarRolRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v2/usuarios/1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asignarRolRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.mensaje").value("Rol asignado correctamente al usuario"))
                .andExpect(jsonPath("$._links.usuario.href").exists())
                .andExpect(jsonPath("$._links.roles.href").exists());

        verify(usuarioService).asignarRol(eq(1L), any(AsignarRolRequestDto.class));
    }

    @Test
    void quitarRol_deberiaRetornar200() throws Exception {
        MessageResponseDto response = new MessageResponseDto("Rol quitado correctamente del usuario");

        when(usuarioService.quitarRol(1L, 2L)).thenReturn(response);

        mockMvc.perform(delete("/api/v2/usuarios/1/roles/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.mensaje").value("Rol quitado correctamente del usuario"))
                .andExpect(jsonPath("$._links.usuario.href").exists())
                .andExpect(jsonPath("$._links.roles.href").exists());

        verify(usuarioService).quitarRol(1L, 2L);
    }

    @Test
    void listarRoles_deberiaRetornar200() throws Exception {
        when(usuarioService.listarRolesDeUsuario(1L)).thenReturn(List.of("ADMIN", "USER"));

        mockMvc.perform(get("/api/v2/usuarios/1/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.stringList[0]").value("ADMIN"))
                .andExpect(jsonPath("$._embedded.stringList[1]").value("USER"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.usuario.href").exists());

        verify(usuarioService).listarRolesDeUsuario(1L);
    }

    @Test
    void crear_deberiaRetornar400CuandoFaltaBodyValido() throws Exception {
        UsuarioRequestDto invalido = new UsuarioRequestDto();

        mockMvc.perform(post("/api/v2/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).crear(any());
    }

    @Test
    void asignarRol_deberiaRetornar400CuandoFaltaRolId() throws Exception {
        AsignarRolRequestDto invalido = new AsignarRolRequestDto();

        mockMvc.perform(post("/api/v2/usuarios/1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).asignarRol(anyLong(), any());
    }
}