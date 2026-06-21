package com.example.ms_usuario.service.impl;

import com.example.ms_usuario.model.Rol;
import com.example.ms_usuario.model.Usuario;
import com.example.ms_usuario.model.UsuarioRol;
import com.example.ms_usuario.repository.RolRepository;
import com.example.ms_usuario.repository.UsuarioRepository;
import com.example.ms_usuario.repository.UsuarioRolRepository;
import com.example.ms_usuario.service.UsuarioRolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UsuarioRolServiceImpl.class)
@ActiveProfiles("test")
public class UsuarioRolServiceImplTest {

    @Autowired
    private UsuarioRolService usuarioRolService;

    @MockitoBean
    private UsuarioRolRepository usuarioRolRepository;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private RolRepository rolRepository;

    @Test
    public void testAsignarRol() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Rol rol = new Rol();
        rol.setId(2L);
        rol.setNombre("ADMIN");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(2L)).thenReturn(Optional.of(rol));
        when(usuarioRolRepository.existsByUsuarioIdAndRolId(1L, 2L)).thenReturn(false);

        usuarioRolService.asignarRol(1L, 2L);

        verify(usuarioRolRepository, times(1)).save(any(UsuarioRol.class));
    }

    @Test
    public void testAsignarRolUsuarioNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioRolService.asignarRol(1L, 2L));
    }

    @Test
    public void testQuitarRol() {
        when(usuarioRolRepository.existsByUsuarioIdAndRolId(1L, 2L)).thenReturn(true);

        usuarioRolService.quitarRol(1L, 2L);

        verify(usuarioRolRepository, times(1)).deleteByUsuarioIdAndRolId(1L, 2L);
    }

    @Test
    public void testListarRolesDeUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Rol rol = new Rol();
        rol.setId(2L);
        rol.setNombre("ADMIN");

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(List.of(usuarioRol));

        List<String> resultado = usuarioRolService.listarRolesDeUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ADMIN", resultado.get(0));
    }
}