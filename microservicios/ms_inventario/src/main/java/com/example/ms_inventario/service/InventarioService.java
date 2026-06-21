package com.example.ms_inventario.service;

import com.example.ms_inventario.dto.InventarioRequestDto;
import com.example.ms_inventario.dto.InventarioResponseDto;
import com.example.ms_inventario.dto.InventarioUpdateDto;
import com.example.ms_inventario.dto.MessageResponseDto;

import java.util.List;

public interface InventarioService {
    InventarioResponseDto crear(InventarioRequestDto dto);
    List<InventarioResponseDto> listar();
    InventarioResponseDto obtenerPorId(Long id);
    InventarioResponseDto obtenerPorProductoId(Long productoId);
    InventarioResponseDto actualizar(Long id, InventarioUpdateDto dto);
    MessageResponseDto eliminar(Long id);
}
