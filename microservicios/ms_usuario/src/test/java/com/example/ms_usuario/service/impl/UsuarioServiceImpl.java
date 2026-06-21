package com.example.ms_usuario.service.impl;

import com.example.ms_usuario.dto.AsignarRolRequestDto;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.UsuarioRequestDto;
import com.example.ms_usuario.dto.UsuarioResponseDto;
import com.example.ms_usuario.dto.UsuarioUpdateDto;
import com.example.ms_usuario.exception.BusinessException;
import com.example.ms_usuario.exception.DuplicateResourceException;
import com.example.ms_usuario.exception.ResourceNotFoundException;
import com.example.ms_usuario.model.Empleado;
import com.example.ms_usuario.model.Rol;
import com.example.ms_usuario.model.Usuario;
import com.example.ms_usuario.model.UsuarioRol;
import com.example.ms_usuario.repository.EmpleadoRepository;
import com.example.ms_usuario.repository.RolRepository;
import com.example.ms_usuario.repository.UsuarioRepository;
import com.example.ms_usuario.repository.UsuarioRolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRolRepository usuarioRolRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UsuarioRequestDto requestDto;
    private UsuarioUpdateDto updateDto;
    private Rol rol;
    private UsuarioRol usuarioRol;
    private AsignarRolRequestDto asignarRolRequestDto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Ignacio");
        usuario.setEmail("ignacio@test.com");
        usuario.setPasswordHash("hashedPassword");
        usuario.setActivo(true);

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

        rol = new Rol();
        rol.setId(2L);
        rol.setNombre("ADMIN");

        usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

        asignarRolRequestDto = new AsignarRolRequestDto();
        asignarRolRequestDto.setRolId(2L);
    }

    @Test
    void crear_deberiaCrearUsuarioCorrectamente() {
        when(usuarioRepository.existsByEmail("ignacio@test.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashed123456");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(List.of());
        when(empleadoRepository.existsByUsuarioId(1L)).thenReturn(false);

        UsuarioResponseDto resultado = usuarioService.crear(requestDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ignacio", resultado.getNombre());
        assertEquals("ignacio@test.com", resultado.getEmail());
        assertTrue(resultado.getActivo());
        assertNotNull(resultado.getRoles());
        assertTrue(resultado.getRoles().isEmpty());
        assertFalse(resultado.getEsEmpleado());

        verify(usuarioRepository).existsByEmail("ignacio@test.com");
        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioRolRepository).findByUsuarioId(1L);
        verify(empleadoRepository).existsByUsuarioId(1L);
    }

    @Test
    void crear_deberiaLanzarDuplicateResourceExceptionSiEmailYaExiste() {
        when(usuarioRepository.existsByEmail("ignacio@test.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.crear(requestDto));

        verify(usuarioRepository).existsByEmail("ignacio@test.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void listar_deberiaRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(List.of(usuarioRol));
        when(empleadoRepository.existsByUsuarioId(1L)).thenReturn(true);

        List<UsuarioResponseDto> resultado = usuarioService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Ignacio", resultado.get(0).getNombre());
        assertEquals("ignacio@test.com", resultado.get(0).getEmail());
        assertTrue(resultado.get(0).getActivo());
        assertEquals(1, resultado.get(0).getRoles().size());
        assertEquals("ADMIN", resultado.get(0).getRoles().get(0));
        assertTrue(resultado.get(0).getEsEmpleado());

        verify(usuarioRepository).findAll();
        verify(usuarioRolRepository).findByUsuarioId(1L);
        verify(empleadoRepository).existsByUsuarioId(1L);
    }

    @Test
    void obtenerPorId_deberiaRetornarUsuarioSiExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(List.of(usuarioRol));
        when(empleadoRepository.existsByUsuarioId(1L)).thenReturn(false);

        UsuarioResponseDto resultado = usuarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ignacio", resultado.getNombre());
        assertEquals("ignacio@test.com", resultado.getEmail());
        assertEquals(1, resultado.getRoles().size());
        assertEquals("ADMIN", resultado.getRoles().get(0));
        assertFalse(resultado.getEsEmpleado());

        verify(usuarioRepository).findById(1L);
        verify(usuarioRolRepository).findByUsuarioId(1L);
        verify(empleadoRepository).existsByUsuarioId(1L);
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcionSiNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.obtenerPorId(1L));

        verify(usuarioRepository).findById(1L);
        verify(usuarioRolRepository, never()).findByUsuarioId(anyLong());
        verify(empleadoRepository, never()).existsByUsuarioId(anyLong());
    }

    @Test
    void actualizar_deberiaActualizarUsuarioYPasswordSiSeEntregaPassword() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailAndIdNot("ignacio.actualizado@test.com", 1L)).thenReturn(false);
        when(passwordEncoder.encode("nuevaPassword")).thenReturn("hashedNuevaPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(List.of(usuarioRol));
        when(empleadoRepository.existsByUsuarioId(1L)).thenReturn(false);

        UsuarioResponseDto resultado = usuarioService.actualizar(1L, updateDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ignacio Actualizado", resultado.getNombre());
        assertEquals("ignacio.actualizado@test.com", resultado.getEmail());
        assertFalse(resultado.getActivo());
        assertEquals(1, resultado.getRoles().size());
        assertEquals("ADMIN", resultado.getRoles().get(0));

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).existsByEmailAndIdNot("ignacio.actualizado@test.com", 1L);
        verify(passwordEncoder).encode("nuevaPassword");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizar_noDebeCambiarPasswordSiSeEntregaNull() {
        updateDto.setPassword(null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailAndIdNot("ignacio.actualizado@test.com", 1L)).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(List.of());
        when(empleadoRepository.existsByUsuarioId(1L)).thenReturn(false);

        UsuarioResponseDto resultado = usuarioService.actualizar(1L, updateDto);

        assertNotNull(resultado);
        assertEquals("Ignacio Actualizado", resultado.getNombre());
        assertEquals("ignacio.actualizado@test.com", resultado.getEmail());
        assertFalse(resultado.getActivo());

        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizar_noDebeCambiarPasswordSiSeEntregaBlank() {
        updateDto.setPassword("   ");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailAndIdNot("ignacio.actualizado@test.com", 1L)).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(List.of());
        when(empleadoRepository.existsByUsuarioId(1L)).thenReturn(false);

        UsuarioResponseDto resultado = usuarioService.actualizar(1L, updateDto);

        assertNotNull(resultado);
        assertEquals("Ignacio Actualizado", resultado.getNombre());

        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizar_deberiaLanzarDuplicateResourceExceptionSiEmailPerteneceAOtroUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailAndIdNot("ignacio.actualizado@test.com", 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.actualizar(1L, updateDto));

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).existsByEmailAndIdNot("ignacio.actualizado@test.com", 1L);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void eliminar_deberiaEliminarEmpleadoRolesYUsuario() {
        Empleado empleado = new Empleado();
        empleado.setId(100L);
        empleado.setUsuario(usuario);
        empleado.setActivo(true);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(empleadoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(empleado));

        MessageResponseDto resultado = usuarioService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Usuario eliminado correctamente", resultado.getMensaje());

        verify(usuarioRepository).findById(1L);
        verify(empleadoRepository).findByUsuarioId(1L);
        verify(empleadoRepository).delete(empleado);
        verify(usuarioRolRepository).deleteByUsuarioId(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void eliminar_deberiaEliminarSoloRolesYUsuarioSiNoEsEmpleado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(empleadoRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());

        MessageResponseDto resultado = usuarioService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Usuario eliminado correctamente", resultado.getMensaje());

        verify(usuarioRepository).findById(1L);
        verify(empleadoRepository).findByUsuarioId(1L);
        verify(empleadoRepository, never()).delete(any());
        verify(usuarioRolRepository).deleteByUsuarioId(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.eliminar(1L));

        verify(usuarioRepository).findById(1L);
        verify(empleadoRepository, never()).findByUsuarioId(anyLong());
        verify(usuarioRolRepository, never()).deleteByUsuarioId(anyLong());
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void asignarRol_deberiaAsignarRolCorrectamente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(2L)).thenReturn(Optional.of(rol));
        when(usuarioRolRepository.existsByUsuarioIdAndRolId(1L, 2L)).thenReturn(false);

        MessageResponseDto resultado = usuarioService.asignarRol(1L, asignarRolRequestDto);

        assertNotNull(resultado);
        assertEquals("Rol asignado correctamente al usuario", resultado.getMensaje());

        verify(usuarioRepository).findById(1L);
        verify(rolRepository).findById(2L);
        verify(usuarioRolRepository).existsByUsuarioIdAndRolId(1L, 2L);
        verify(usuarioRolRepository).save(any(UsuarioRol.class));
    }

    @Test
    void asignarRol_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.asignarRol(1L, asignarRolRequestDto));

        verify(usuarioRepository).findById(1L);
        verify(rolRepository, never()).findById(anyLong());
        verify(usuarioRolRepository, never()).save(any());
    }

    @Test
    void asignarRol_deberiaLanzarExcepcionSiRolNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.asignarRol(1L, asignarRolRequestDto));

        verify(usuarioRepository).findById(1L);
        verify(rolRepository).findById(2L);
        verify(usuarioRolRepository, never()).save(any());
    }

    @Test
    void asignarRol_deberiaLanzarBusinessExceptionSiRelacionYaExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(2L)).thenReturn(Optional.of(rol));
        when(usuarioRolRepository.existsByUsuarioIdAndRolId(1L, 2L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> usuarioService.asignarRol(1L, asignarRolRequestDto));

        verify(usuarioRepository).findById(1L);
        verify(rolRepository).findById(2L);
        verify(usuarioRolRepository).existsByUsuarioIdAndRolId(1L, 2L);
        verify(usuarioRolRepository, never()).save(any());
    }

    @Test
    void quitarRol_deberiaQuitarRolCorrectamente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRolRepository.existsByUsuarioIdAndRolId(1L, 2L)).thenReturn(true);

        MessageResponseDto resultado = usuarioService.quitarRol(1L, 2L);

        assertNotNull(resultado);
        assertEquals("Rol quitado correctamente del usuario", resultado.getMensaje());

        verify(usuarioRepository).findById(1L);
        verify(usuarioRolRepository).existsByUsuarioIdAndRolId(1L, 2L);
        verify(usuarioRolRepository).deleteByUsuarioIdAndRolId(1L, 2L);
    }

    @Test
    void quitarRol_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.quitarRol(1L, 2L));

        verify(usuarioRepository).findById(1L);
        verify(usuarioRolRepository, never()).existsByUsuarioIdAndRolId(anyLong(), anyLong());
        verify(usuarioRolRepository, never()).deleteByUsuarioIdAndRolId(anyLong(), anyLong());
    }

    @Test
    void quitarRol_deberiaLanzarExcepcionSiRelacionNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRolRepository.existsByUsuarioIdAndRolId(1L, 2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.quitarRol(1L, 2L));

        verify(usuarioRepository).findById(1L);
        verify(usuarioRolRepository).existsByUsuarioIdAndRolId(1L, 2L);
        verify(usuarioRolRepository, never()).deleteByUsuarioIdAndRolId(anyLong(), anyLong());
    }

    @Test
    void listarRolesDeUsuario_deberiaRetornarNombresDeRoles() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRolRepository.findByUsuarioId(1L)).thenReturn(List.of(usuarioRol));

        List<String> resultado = usuarioService.listarRolesDeUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ADMIN", resultado.get(0));

        verify(usuarioRepository).findById(1L);
        verify(usuarioRolRepository).findByUsuarioId(1L);
    }

    @Test
    void listarRolesDeUsuario_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.listarRolesDeUsuario(1L));

        verify(usuarioRepository).findById(1L);
        verify(usuarioRolRepository, never()).findByUsuarioId(anyLong());
    }
}