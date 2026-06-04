package com.example.ms_usuario.service;

import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.PermisoRequestDto;
import com.example.ms_usuario.dto.PermisoResponseDto;

import java.util.List;

public interface PermisoService {
    PermisoResponseDto crear(PermisoRequestDto dto);
    List<PermisoResponseDto> listar();
    PermisoResponseDto obtenerPorId(Long id);
    PermisoResponseDto actualizar(Long id, PermisoRequestDto dto);
    MessageResponseDto eliminar(Long id);
}
