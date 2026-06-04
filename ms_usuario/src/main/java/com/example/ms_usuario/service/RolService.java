package com.example.ms_usuario.service;

import com.example.ms_usuario.dto.MessageResponseDto;
import com.example.ms_usuario.dto.RolRequestDto;
import com.example.ms_usuario.dto.RolResponseDto;

import java.util.List;

public interface RolService {
    RolResponseDto crear(RolRequestDto dto);
    List<RolResponseDto> listar();
    RolResponseDto obtenerPorId(Long id);
    RolResponseDto actualizar(Long id, RolRequestDto dto);
    MessageResponseDto eliminar(Long id);
}
