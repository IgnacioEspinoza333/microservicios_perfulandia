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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    @Override
    public RolResponseDto crear(RolRequestDto dto) {
        if (rolRepository.existsByNombre(dto.getNombre())) {
            throw new DuplicateResourceException("Ya existe un rol con ese nombre");
        }

        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        rol.setPermisos(getPermisos(dto.getPermisoIds()));

        rol = rolRepository.save(rol);
        return mapToResponse(rol);
    }

    @Override
    public List<RolResponseDto> listar() {
        return rolRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public RolResponseDto obtenerPorId(Long id) {
        Rol rol = getRolOrThrow(id);
        return mapToResponse(rol);
    }

    @Override
    public RolResponseDto actualizar(Long id, RolRequestDto dto) {
        Rol rol = getRolOrThrow(id);

        if (rolRepository.existsByNombreAndIdNot(dto.getNombre(), id)) {
            throw new DuplicateResourceException("Ya existe otro rol con ese nombre");
        }

        rol.setNombre(dto.getNombre());
        rol.setPermisos(getPermisos(dto.getPermisoIds()));

        rol = rolRepository.save(rol);
        return mapToResponse(rol);
    }

    @Override
    public MessageResponseDto eliminar(Long id) {
        Rol rol = getRolOrThrow(id);

        usuarioRolRepository.deleteByRolId(id);
        rol.getPermisos().clear();
        rolRepository.save(rol);

        rolRepository.delete(rol);

        return new MessageResponseDto("Rol eliminado correctamente");
    }

    private Rol getRolOrThrow(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
    }

    private Set<Permiso> getPermisos(Set<Long> permisoIds) {
        Set<Permiso> permisos = new HashSet<>();

        if (permisoIds != null && !permisoIds.isEmpty()) {
            for (Long permisoId : permisoIds) {
                Permiso permiso = permisoRepository.findById(permisoId)
                        .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con id: " + permisoId));
                permisos.add(permiso);
            }
        }

        return permisos;
    }

    private RolResponseDto mapToResponse(Rol rol) {
        Set<String> permisos = rol.getPermisos()
                .stream()
                .map(Permiso::getCodigo)
                .collect(java.util.stream.Collectors.toSet());

        return new RolResponseDto(
                rol.getId(),
                rol.getNombre(),
                permisos
        );
    }
}