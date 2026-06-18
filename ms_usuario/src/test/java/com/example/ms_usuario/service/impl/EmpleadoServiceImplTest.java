package com.example.ms_usuario.service.impl;

import com.example.ms_usuario.dto.EmpleadoRequestDto;
import com.example.ms_usuario.dto.EmpleadoResponseDto;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.exception.BusinessException;
import com.example.ms_usuario.exception.ResourceNotFoundException;
import com.example.ms_usuario.model.Empleado;
import com.example.ms_usuario.model.Usuario;
import com.example.ms_usuario.repository.EmpleadoRepository;
import com.example.ms_usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceImplTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Usuario usuario1;
    private Usuario usuario2;
    private Empleado empleado;
    private EmpleadoRequestDto requestDto;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId(10L);
        usuario1.setNombre("Ignacio");
        usuario1.setEmail("ignacio@test.com");

        usuario2 = new Usuario();
        usuario2.setId(20L);
        usuario2.setNombre("Camila");
        usuario2.setEmail("camila@test.com");

        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setUsuario(usuario1);
        empleado.setActivo(true);

        requestDto = new EmpleadoRequestDto();
        requestDto.setUsuarioId(10L);
        requestDto.setActivo(true);
    }

    @Test
    void crear_deberiaCrearEmpleadoCorrectamente() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario1));
        when(empleadoRepository.existsByUsuarioId(10L)).thenReturn(false);
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> {
            Empleado e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        EmpleadoResponseDto resultado = empleadoService.crear(requestDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getUsuarioId());
        assertEquals("Ignacio", resultado.getNombreUsuario());
        assertEquals("ignacio@test.com", resultado.getEmailUsuario());
        assertTrue(resultado.getActivo());

        verify(usuarioRepository).findById(10L);
        verify(empleadoRepository).existsByUsuarioId(10L);
        verify(empleadoRepository).save(any(Empleado.class));
    }

    @Test
    void crear_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> empleadoService.crear(requestDto));

        verify(usuarioRepository).findById(10L);
        verify(empleadoRepository, never()).existsByUsuarioId(anyLong());
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void crear_deberiaLanzarBusinessExceptionSiUsuarioYaEsEmpleado() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario1));
        when(empleadoRepository.existsByUsuarioId(10L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> empleadoService.crear(requestDto));

        verify(usuarioRepository).findById(10L);
        verify(empleadoRepository).existsByUsuarioId(10L);
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void listar_deberiaRetornarListaDeEmpleados() {
        when(empleadoRepository.findAll()).thenReturn(List.of(empleado));

        List<EmpleadoResponseDto> resultado = empleadoService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(10L, resultado.get(0).getUsuarioId());
        assertEquals("Ignacio", resultado.get(0).getNombreUsuario());
        assertEquals("ignacio@test.com", resultado.get(0).getEmailUsuario());
        assertTrue(resultado.get(0).getActivo());

        verify(empleadoRepository).findAll();
    }

    @Test
    void obtenerPorId_deberiaRetornarEmpleadoSiExiste() {
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));

        EmpleadoResponseDto resultado = empleadoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getUsuarioId());
        assertEquals("Ignacio", resultado.getNombreUsuario());
        assertEquals("ignacio@test.com", resultado.getEmailUsuario());
        assertTrue(resultado.getActivo());

        verify(empleadoRepository).findById(1L);
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcionSiNoExiste() {
        when(empleadoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> empleadoService.obtenerPorId(1L));

        verify(empleadoRepository).findById(1L);
    }

    @Test
    void actualizar_deberiaActualizarEmpleadoCorrectamente() {
        EmpleadoRequestDto updateDto = new EmpleadoRequestDto();
        updateDto.setUsuarioId(20L);
        updateDto.setActivo(false);

        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(usuarioRepository.findById(20L)).thenReturn(Optional.of(usuario2));
        when(empleadoRepository.existsByUsuarioId(20L)).thenReturn(false);
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmpleadoResponseDto resultado = empleadoService.actualizar(1L, updateDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(20L, resultado.getUsuarioId());
        assertEquals("Camila", resultado.getNombreUsuario());
        assertEquals("camila@test.com", resultado.getEmailUsuario());
        assertFalse(resultado.getActivo());

        verify(empleadoRepository).findById(1L);
        verify(usuarioRepository).findById(20L);
        verify(empleadoRepository).existsByUsuarioId(20L);
        verify(empleadoRepository).save(any(Empleado.class));
    }

    @Test
    void actualizar_noDebeValidarDuplicadoSiEsElMismoUsuarioActual() {
        EmpleadoRequestDto updateDto = new EmpleadoRequestDto();
        updateDto.setUsuarioId(10L);
        updateDto.setActivo(false);

        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario1));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmpleadoResponseDto resultado = empleadoService.actualizar(1L, updateDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getUsuarioId());
        assertEquals("Ignacio", resultado.getNombreUsuario());
        assertEquals("ignacio@test.com", resultado.getEmailUsuario());
        assertFalse(resultado.getActivo());

        verify(empleadoRepository).findById(1L);
        verify(usuarioRepository).findById(10L);
        verify(empleadoRepository, never()).existsByUsuarioId(10L);
        verify(empleadoRepository).save(any(Empleado.class));
    }

    @Test
    void actualizar_deberiaLanzarBusinessExceptionSiNuevoUsuarioYaEstaAsociadoAOtroEmpleado() {
        EmpleadoRequestDto updateDto = new EmpleadoRequestDto();
        updateDto.setUsuarioId(20L);
        updateDto.setActivo(true);

        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(usuarioRepository.findById(20L)).thenReturn(Optional.of(usuario2));
        when(empleadoRepository.existsByUsuarioId(20L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> empleadoService.actualizar(1L, updateDto));

        verify(empleadoRepository).findById(1L);
        verify(usuarioRepository).findById(20L);
        verify(empleadoRepository).existsByUsuarioId(20L);
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void eliminar_deberiaEliminarEmpleadoCorrectamente() {
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));

        MessageResponseDto resultado = empleadoService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Empleado eliminado correctamente", resultado.getMensaje());

        verify(empleadoRepository).findById(1L);
        verify(empleadoRepository).delete(empleado);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiEmpleadoNoExiste() {
        when(empleadoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> empleadoService.eliminar(1L));

        verify(empleadoRepository).findById(1L);
        verify(empleadoRepository, never()).delete(any());
    }
}
