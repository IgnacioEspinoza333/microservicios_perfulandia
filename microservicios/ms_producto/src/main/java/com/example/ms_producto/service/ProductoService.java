package com.example.ms_producto.service;

import com.example.ms_producto.dto.MessageResponseDto;
import com.example.ms_producto.dto.ProductoRequestDto;
import com.example.ms_producto.dto.ProductoResponseDto;
import com.example.ms_producto.dto.ProductoResumenDto;
import com.example.ms_producto.dto.ProductoUpdateDto;

import java.util.List;

public interface ProductoService {
    ProductoResponseDto crear(ProductoRequestDto dto);
    List<ProductoResponseDto> listar();
    ProductoResponseDto obtenerPorId(Long id);
    ProductoResumenDto obtenerResumenPorId(Long id);
    ProductoResponseDto actualizar(Long id, ProductoUpdateDto dto);
    MessageResponseDto eliminar(Long id);
}