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
import com.example.ms_usuario.service.PermisoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PermisoServiceImpl implements PermisoService {

    private final PermisoRepository permisoRepository;
    private final RolRepository rolRepository;

    @Override
    public PermisoResponseDto crear(PermisoRequestDto dto) {
        if (permisoRepository.existsByCodigo(dto.getCodigo())) {
            throw new DuplicateResourceException("Ya existe un permiso con ese código");
        }

        Permiso permiso = new Permiso();
        permiso.setCodigo(dto.getCodigo());

        permiso = permisoRepository.save(permiso);
        return mapToResponse(permiso);
    }

    @Override
    public List<PermisoResponseDto> listar() {
        return permisoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PermisoResponseDto obtenerPorId(Long id) {
        Permiso permiso = getPermisoOrThrow(id);
        return mapToResponse(permiso);
    }

    @Override
    public PermisoResponseDto actualizar(Long id, PermisoRequestDto dto) {
        Permiso permiso = getPermisoOrThrow(id);

        if (permisoRepository.existsByCodigoAndIdNot(dto.getCodigo(), id)) {
            throw new DuplicateResourceException("Ya existe otro permiso con ese código");
        }

        permiso.setCodigo(dto.getCodigo());
        permiso = permisoRepository.save(permiso);

        return mapToResponse(permiso);
    }

    @Override
    public MessageResponseDto eliminar(Long id) {
        Permiso permiso = getPermisoOrThrow(id);

        List<Rol> roles = rolRepository.findAll();
        for (Rol rol : roles) {
            rol.getPermisos().removeIf(p -> p.getId().equals(id));
            rolRepository.save(rol);
        }

        permisoRepository.delete(permiso);

        return new MessageResponseDto("Permiso eliminado correctamente");
    }

    private Permiso getPermisoOrThrow(Long id) {
        return permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con id: " + id));
    }

    private PermisoResponseDto mapToResponse(Permiso permiso) {
        return new PermisoResponseDto(permiso.getId(), permiso.getCodigo());
    }
}
