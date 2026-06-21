package com.example.ms_proveedores.service;

import com.example.ms_proveedores.dto.AbastecimientoRequestDto;
import com.example.ms_proveedores.dto.AbastecimientoResponseDto;
import com.example.ms_proveedores.dto.AbastecimientoUpdateDto;
import com.example.ms_proveedores.dto.MessageResponseDto;

import java.util.List;

public interface AbastecimientoService {
    AbastecimientoResponseDto crear(AbastecimientoRequestDto dto);
    List<AbastecimientoResponseDto> listar();
    List<AbastecimientoResponseDto> listarPorProveedor(Long proveedorId);
    List<AbastecimientoResponseDto> listarPorProducto(Long productoId);
    AbastecimientoResponseDto obtenerPorId(Long id);
    AbastecimientoResponseDto actualizar(Long id, AbastecimientoUpdateDto dto);
    MessageResponseDto eliminar(Long id);
}
