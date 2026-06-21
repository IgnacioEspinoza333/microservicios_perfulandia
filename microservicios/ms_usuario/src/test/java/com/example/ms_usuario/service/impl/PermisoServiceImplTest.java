package com.example.ms_usuario.service.impl;

import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.PermisoRequestDto;
import com.example.ms_usuario.dto.PermisoResponseDto;
import com.example.ms_usuario.exception.DuplicateResourceException;
import com.example.ms_usuario.exception.ResourceNotFoundException;
import com.example.ms_usuario.model.Permiso;
import com.example.ms_usuario.model.Rol;
import com.example.ms_usuario.repository.PermisoRepository;
import com.example.ms_usuario.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermisoServiceImplTest {

    @Mock
    private PermisoRepository permisoRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private PermisoServiceImpl permisoService;

    private Permiso permiso;
    private PermisoRequestDto requestDto;

    @BeforeEach
    void setUp() {
        permiso = new Permiso();
        permiso.setId(1L);
        permiso.setCodigo("PERM_ADMIN");

        requestDto = new PermisoRequestDto();
        requestDto.setCodigo("PERM_ADMIN");
    }

    @Test
    void crear_deberiaCrearPermisoCorrectamente() {
        when(permisoRepository.existsByCodigo("PERM_ADMIN")).thenReturn(false);
        when(permisoRepository.save(any(Permiso.class))).thenAnswer(invocation -> {
            Permiso p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        PermisoResponseDto resultado = permisoService.crear(requestDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PERM_ADMIN", resultado.getCodigo());

        verify(permisoRepository).existsByCodigo("PERM_ADMIN");
        verify(permisoRepository).save(any(Permiso.class));
    }

    @Test
    void crear_deberiaLanzarExceptionSiCodigoExiste() {
        when(permisoRepository.existsByCodigo("PERM_ADMIN")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> permisoService.crear(requestDto));

        verify(permisoRepository).existsByCodigo("PERM_ADMIN");
        verify(permisoRepository, never()).save(any());
    }

    @Test
    void listar_deberiaRetornarListaDePermisos() {
        when(permisoRepository.findAll()).thenReturn(List.of(permiso));

        List<PermisoResponseDto> resultado = permisoService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("PERM_ADMIN", resultado.get(0).getCodigo());

        verify(permisoRepository).findAll();
    }

    @Test
    void obtenerPorId_deberiaRetornarPermiso() {
        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permiso));

        PermisoResponseDto resultado = permisoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PERM_ADMIN", resultado.getCodigo());

        verify(permisoRepository).findById(1L);
    }

    @Test
    void obtenerPorId_deberiaLanzarExceptionSiNoExiste() {
        when(permisoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> permisoService.obtenerPorId(1L));

        verify(permisoRepository).findById(1L);
    }

    @Test
    void actualizar_deberiaActualizarPermiso() {
        PermisoRequestDto updateDto = new PermisoRequestDto();
        updateDto.setCodigo("PERM_UPDATED");

        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permiso));
        when(permisoRepository.existsByCodigoAndIdNot("PERM_UPDATED", 1L)).thenReturn(false);
        when(permisoRepository.save(any(Permiso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PermisoResponseDto resultado = permisoService.actualizar(1L, updateDto);

        assertNotNull(resultado);
        assertEquals("PERM_UPDATED", resultado.getCodigo());

        verify(permisoRepository).findById(1L);
        verify(permisoRepository).existsByCodigoAndIdNot("PERM_UPDATED", 1L);
        verify(permisoRepository).save(any(Permiso.class));
    }

    @Test
    void actualizar_deberiaLanzarExceptionSiCodigoDuplicado() {
        PermisoRequestDto updateDto = new PermisoRequestDto();
        updateDto.setCodigo("PERM_DUP");

        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permiso));
        when(permisoRepository.existsByCodigoAndIdNot("PERM_DUP", 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> permisoService.actualizar(1L, updateDto));

        verify(permisoRepository).findById(1L);
        verify(permisoRepository).existsByCodigoAndIdNot("PERM_DUP", 1L);
        verify(permisoRepository, never()).save(any());
    }

    @Test
    void eliminar_deberiaEliminarPermisoYActualizarRoles() {
        Rol rol = new Rol();
        rol.setId(10L);

        rol.setPermisos(new HashSet<>());
        rol.getPermisos().add(permiso);

        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permiso));
        when(rolRepository.findAll()).thenReturn(List.of(rol));

        MessageResponseDto resultado = permisoService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Permiso eliminado correctamente", resultado.getMensaje());

        verify(permisoRepository).findById(1L);
        verify(rolRepository).findAll();
        verify(rolRepository).save(any(Rol.class));
        verify(permisoRepository).delete(permiso);

        assertTrue(rol.getPermisos().isEmpty());
    }


    @Test
    void eliminar_deberiaLanzarExceptionSiPermisoNoExiste() {
        when(permisoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> permisoService.eliminar(1L));

        verify(permisoRepository).findById(1L);
        verify(rolRepository, never()).findAll();
        verify(permisoRepository, never()).delete(any());
    }
}
