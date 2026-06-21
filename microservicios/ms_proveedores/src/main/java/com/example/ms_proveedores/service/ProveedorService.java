package com.example.ms_proveedores.service;

import com.example.ms_proveedores.dto.MessageResponseDto;
import com.example.ms_proveedores.dto.ProveedorRequestDto;
import com.example.ms_proveedores.dto.ProveedorResponseDto;
import com.example.ms_proveedores.dto.ProveedorUpdateDto;

import java.util.List;

public interface ProveedorService {
    ProveedorResponseDto crear(ProveedorRequestDto dto);
    List<ProveedorResponseDto> listar();
    ProveedorResponseDto obtenerPorId(Long id);
    ProveedorResponseDto actualizar(Long id, ProveedorUpdateDto dto);
    MessageResponseDto eliminar(Long id);
}