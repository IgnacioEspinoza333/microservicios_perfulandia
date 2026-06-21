package com.example.ms_clientes.service;

import com.example.ms_clientes.dto.DireccionRequestDto;
import com.example.ms_clientes.dto.DireccionResponseDto;
import com.example.ms_clientes.dto.DireccionUpdateDto;
import com.example.ms_clientes.dto.MessageResponseDto;

import java.util.List;

public interface DireccionService {
    DireccionResponseDto crear(Long clienteId, DireccionRequestDto dto);
    List<DireccionResponseDto> listar();
    List<DireccionResponseDto> listarPorCliente(Long clienteId);
    DireccionResponseDto obtenerPorId(Long id);
    DireccionResponseDto actualizar(Long id, DireccionUpdateDto dto);
    MessageResponseDto eliminar(Long id);
}