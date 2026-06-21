package com.example.ms_clientes.service;

import com.example.ms_clientes.dto.ClienteRequestDto;
import com.example.ms_clientes.dto.ClienteResponseDto;
import com.example.ms_clientes.dto.ClienteUpdateDto;
import com.example.ms_clientes.dto.MessageResponseDto;

import java.util.List;

public interface ClienteService {
    ClienteResponseDto crear(ClienteRequestDto dto);
    List<ClienteResponseDto> listar();
    ClienteResponseDto obtenerPorId(Long id);
    ClienteResponseDto actualizar(Long id, ClienteUpdateDto dto);
    MessageResponseDto eliminar(Long id);
}