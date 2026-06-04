package com.example.ms_usuario.service;

import com.example.ms_usuario.dto.AsignarRolRequestDto;
import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.UsuarioRequestDto;
import com.example.ms_usuario.dto.UsuarioResponseDto;
import com.example.ms_usuario.dto.UsuarioUpdateDto;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDto crear(UsuarioRequestDto dto);
    List<UsuarioResponseDto> listar();
    UsuarioResponseDto obtenerPorId(Long id);
    UsuarioResponseDto actualizar(Long id, UsuarioUpdateDto dto);
    MessageResponseDto eliminar(Long id);
    MessageResponseDto asignarRol(Long usuarioId, AsignarRolRequestDto dto);
    MessageResponseDto quitarRol(Long usuarioId, Long rolId);
    List<String> listarRolesDeUsuario(Long usuarioId);
}

