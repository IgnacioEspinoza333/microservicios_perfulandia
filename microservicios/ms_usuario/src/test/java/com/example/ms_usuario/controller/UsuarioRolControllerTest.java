package com.example.ms_usuario.controller;

import com.example.ms_usuario.service.UsuarioRolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioRolController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioRolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioRolService usuarioRolService;

    @Test
    public void testAsignarRol() throws Exception {
        doNothing().when(usuarioRolService).asignarRol(1L, 2L);

        mockMvc.perform(post("/api/usuarios/1/roles/2"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Rol asignado correctamente al usuario."));

        verify(usuarioRolService, times(1)).asignarRol(1L, 2L);
    }

    @Test
    public void testQuitarRol() throws Exception {
        doNothing().when(usuarioRolService).quitarRol(1L, 2L);

        mockMvc.perform(delete("/api/usuarios/1/roles/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rol quitado correctamente del usuario."));

        verify(usuarioRolService, times(1)).quitarRol(1L, 2L);
    }

    @Test
    public void testListarRolesDeUsuario() throws Exception {
        when(usuarioRolService.listarRolesDeUsuario(1L)).thenReturn(List.of("ADMIN", "USER"));

        mockMvc.perform(get("/api/usuarios/1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("ADMIN"))
                .andExpect(jsonPath("$[1]").value("USER"));

        verify(usuarioRolService, times(1)).listarRolesDeUsuario(1L);
    }
}