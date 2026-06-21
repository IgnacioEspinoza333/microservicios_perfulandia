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
import com.example.ms_usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolRepository rolRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDto crear(UsuarioRequestDto dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuario.setActivo(dto.getActivo());

        usuario = usuarioRepository.save(usuario);
        return mapToResponse(usuario);
    }

    @Override
    public List<UsuarioResponseDto> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UsuarioResponseDto obtenerPorId(Long id) {
        Usuario usuario = getUsuarioOrThrow(id);
        return mapToResponse(usuario);
    }

    @Override
    public UsuarioResponseDto actualizar(Long id, UsuarioUpdateDto dto) {
        Usuario usuario = getUsuarioOrThrow(id);

        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateResourceException("Ya existe otro usuario con ese email");
        }

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setActivo(dto.getActivo());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        usuario = usuarioRepository.save(usuario);
        return mapToResponse(usuario);
    }

    @Override
    public MessageResponseDto eliminar(Long id) {
        Usuario usuario = getUsuarioOrThrow(id);

        Empleado empleado = empleadoRepository.findByUsuarioId(id).orElse(null);
        if (empleado != null) {
            empleadoRepository.delete(empleado);
        }

        usuarioRolRepository.deleteByUsuarioId(id);
        usuarioRepository.delete(usuario);

        return new MessageResponseDto("Usuario eliminado correctamente");
    }

    @Override
    public MessageResponseDto asignarRol(Long usuarioId, AsignarRolRequestDto dto) {
        Usuario usuario = getUsuarioOrThrow(usuarioId);
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + dto.getRolId()));

        if (usuarioRolRepository.existsByUsuarioIdAndRolId(usuarioId, dto.getRolId())) {
            throw new BusinessException("El usuario ya tiene asignado ese rol");
        }

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

        usuarioRolRepository.save(usuarioRol);

        return new MessageResponseDto("Rol asignado correctamente al usuario");
    }

    @Override
    public MessageResponseDto quitarRol(Long usuarioId, Long rolId) {
        getUsuarioOrThrow(usuarioId);

        if (!usuarioRolRepository.existsByUsuarioIdAndRolId(usuarioId, rolId)) {
            throw new ResourceNotFoundException("La relación usuario-rol no existe");
        }

        usuarioRolRepository.deleteByUsuarioIdAndRolId(usuarioId, rolId);
        return new MessageResponseDto("Rol quitado correctamente del usuario");
    }

    @Override
    public List<String> listarRolesDeUsuario(Long usuarioId) {
        getUsuarioOrThrow(usuarioId);

        return usuarioRolRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(ur -> ur.getRol().getNombre())
                .toList();
    }

    private Usuario getUsuarioOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private UsuarioResponseDto mapToResponse(Usuario usuario) {
        List<String> roles = usuarioRolRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(ur -> ur.getRol().getNombre())
                .toList();

        boolean esEmpleado = empleadoRepository.existsByUsuarioId(usuario.getId());

        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getActivo(),
                roles,
                esEmpleado
        );
    }
}
