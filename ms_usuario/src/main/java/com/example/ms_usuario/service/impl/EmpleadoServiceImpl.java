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
import com.example.ms_usuario.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public EmpleadoResponseDto crear(EmpleadoRequestDto dto) {
        Usuario usuario = getUsuarioOrThrow(dto.getUsuarioId());

        if (empleadoRepository.existsByUsuarioId(dto.getUsuarioId())) {
            throw new BusinessException("Ese usuario ya está registrado como empleado");
        }

        Empleado empleado = new Empleado();
        empleado.setUsuario(usuario);
        empleado.setActivo(dto.getActivo());

        empleado = empleadoRepository.save(empleado);
        return mapToResponse(empleado);
    }

    @Override
    public List<EmpleadoResponseDto> listar() {
        return empleadoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EmpleadoResponseDto obtenerPorId(Long id) {
        Empleado empleado = getEmpleadoOrThrow(id);
        return mapToResponse(empleado);
    }

    @Override
    public EmpleadoResponseDto actualizar(Long id, EmpleadoRequestDto dto) {
        Empleado empleado = getEmpleadoOrThrow(id);
        Usuario usuario = getUsuarioOrThrow(dto.getUsuarioId());

        if (!empleado.getUsuario().getId().equals(dto.getUsuarioId())
                && empleadoRepository.existsByUsuarioId(dto.getUsuarioId())) {
            throw new BusinessException("Ese usuario ya está asociado a otro empleado");
        }

        empleado.setUsuario(usuario);
        empleado.setActivo(dto.getActivo());

        empleado = empleadoRepository.save(empleado);
        return mapToResponse(empleado);
    }

    @Override
    public MessageResponseDto eliminar(Long id) {
        Empleado empleado = getEmpleadoOrThrow(id);
        empleadoRepository.delete(empleado);
        return new MessageResponseDto("Empleado eliminado correctamente");
    }

    private Empleado getEmpleadoOrThrow(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
    }

    private Usuario getUsuarioOrThrow(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
    }

    private EmpleadoResponseDto mapToResponse(Empleado empleado) {
        return new EmpleadoResponseDto(
                empleado.getId(),
                empleado.getUsuario().getId(),
                empleado.getUsuario().getNombre(),
                empleado.getUsuario().getEmail(),
                empleado.getActivo()
        );
    }
}