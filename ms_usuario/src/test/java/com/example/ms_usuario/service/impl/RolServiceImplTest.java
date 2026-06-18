package com.example.ms_usuario.service.impl;

import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.RolRequestDto;
import com.example.ms_usuario.dto.RolResponseDto;
import com.example.ms_usuario.exception.DuplicateResourceException;
import com.example.ms_usuario.exception.ResourceNotFoundException;
import com.example.ms_usuario.model.Permiso;
import com.example.ms_usuario.model.Rol;
import com.example.ms_usuario.repository.PermisoRepository;
import com.example.ms_usuario.repository.RolRepository;
import com.example.ms_usuario.repository.UsuarioRolRepository;
import com.example.ms_usuario.service.RolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = RolServiceImpl.class)
@ActiveProfiles("test")
public class RolServiceImplTest {

    @Autowired
    private RolService rolService;

    @MockitoBean
    private RolRepository rolRepository;

    @MockitoBean
    private PermisoRepository permisoRepository;

    @MockitoBean
    private UsuarioRolRepository usuarioRolRepository;

    @Test
    public void testCrear() {
        Permiso permiso = crearPermiso(100L, "PERM_ADMIN");

        RolRequestDto dto = new RolRequestDto();
        dto.setNombre("ADMIN");
        dto.setPermisoIds(Set.of(100L));

        when(rolRepository.existsByNombre("ADMIN")).thenReturn(false);
        when(permisoRepository.findById(100L)).thenReturn(Optional.of(permiso));
        when(rolRepository.save(any(Rol.class))).thenAnswer(invocation -> {
            Rol rol = invocation.getArgument(0);
            rol.setId(1L);
            return rol;
        });

        RolResponseDto resultado = rolService.crear(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("ADMIN", resultado.getNombre());
        assertEquals(1, resultado.getPermisos().size());
        assertTrue(resultado.getPermisos().contains("PERM_ADMIN"));
    }

    @Test
    public void testCrearDuplicado() {
        RolRequestDto dto = new RolRequestDto();
        dto.setNombre("ADMIN");
        dto.setPermisoIds(Set.of());

        when(rolRepository.existsByNombre("ADMIN")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> rolService.crear(dto));
    }

    @Test
    public void testCrearPermisoNoExiste() {
        RolRequestDto dto = new RolRequestDto();
        dto.setNombre("ADMIN");
        dto.setPermisoIds(Set.of(100L));

        when(rolRepository.existsByNombre("ADMIN")).thenReturn(false);
        when(permisoRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rolService.crear(dto));
    }

    @Test
    public void testListar() {
        Permiso permiso = crearPermiso(100L, "PERM_ADMIN");

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
        rol.getPermisos().add(permiso);

        when(rolRepository.findAll()).thenReturn(List.of(rol));

        List<RolResponseDto> resultado = rolService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("ADMIN", resultado.get(0).getNombre());
        assertEquals(1, resultado.get(0).getPermisos().size());
        assertTrue(resultado.get(0).getPermisos().contains("PERM_ADMIN"));
    }

    @Test
    public void testObtenerPorId() {
        Permiso permiso = crearPermiso(100L, "PERM_ADMIN");

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
        rol.getPermisos().add(permiso);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        RolResponseDto resultado = rolService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("ADMIN", resultado.getNombre());
        assertEquals(1, resultado.getPermisos().size());
        assertTrue(resultado.getPermisos().contains("PERM_ADMIN"));
    }

    @Test
    public void testObtenerPorIdNoExiste() {
        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rolService.obtenerPorId(1L));
    }

    @Test
    public void testActualizar() {
        Permiso permisoNuevo = crearPermiso(200L, "PERM_USER");

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
        rol.setPermisos(new HashSet<>());

        RolRequestDto dto = new RolRequestDto();
        dto.setNombre("USER");
        dto.setPermisoIds(Set.of(200L));

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolRepository.existsByNombreAndIdNot("USER", 1L)).thenReturn(false);
        when(permisoRepository.findById(200L)).thenReturn(Optional.of(permisoNuevo));
        when(rolRepository.save(any(Rol.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RolResponseDto resultado = rolService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals("USER", resultado.getNombre());
        assertEquals(1, resultado.getPermisos().size());
        assertTrue(resultado.getPermisos().contains("PERM_USER"));
    }

    @Test
    public void testActualizarDuplicado() {
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");

        RolRequestDto dto = new RolRequestDto();
        dto.setNombre("USER");
        dto.setPermisoIds(Set.of());

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolRepository.existsByNombreAndIdNot("USER", 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> rolService.actualizar(1L, dto));
    }

    @Test
    public void testEliminar() {
        Permiso permiso = crearPermiso(100L, "PERM_ADMIN");

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
        rol.getPermisos().add(permiso);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        MessageResponseDto resultado = rolService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Rol eliminado correctamente", resultado.getMensaje());

        verify(usuarioRolRepository, times(1)).deleteByRolId(1L);
        verify(rolRepository, times(1)).save(rol);
        verify(rolRepository, times(1)).delete(rol);

        assertTrue(rol.getPermisos().isEmpty());
    }

    private Permiso crearPermiso(Long id, String codigo) {
        Permiso permiso = new Permiso();
        permiso.setId(id);
        permiso.setCodigo(codigo);
        return permiso;
    }
}