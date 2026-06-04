package com.example.ms_usuario.service;

import com.example.ms_usuario.dto.EmpleadoRequestDto;
import com.example.ms_usuario.dto.EmpleadoResponseDto;
import com.example.ms_usuario.dto.MessageResponseDto;

import java.util.List;

public interface EmpleadoService {
    EmpleadoResponseDto crear(EmpleadoRequestDto dto);
    List<EmpleadoResponseDto> listar();
    EmpleadoResponseDto obtenerPorId(Long id);
    EmpleadoResponseDto actualizar(Long id, EmpleadoRequestDto dto);
    MessageResponseDto eliminar(Long id);
}