package com.example.ms_categoria.service;

import java.util.List;

import com.example.ms_categoria.dto.CategoriaRequestDTO;
import com.example.ms_categoria.dto.CategoriaResponseDTO;

public interface CategoriaService {
   CategoriaResponseDTO crearCategoria(CategoriaRequestDTO request);
    CategoriaResponseDTO obtenerCategoria(Long id);
    List<CategoriaResponseDTO> listarCategorias();
    void eliminarCategoria(Long id);
}
