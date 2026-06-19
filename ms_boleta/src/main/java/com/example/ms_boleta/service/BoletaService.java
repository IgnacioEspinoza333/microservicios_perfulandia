package com.example.ms_boleta.service;

import java.util.List;

import com.example.ms_boleta.dto.BoletaRequestDTO;
import com.example.ms_boleta.dto.BoletaResponseDTO;

public interface BoletaService {
    BoletaResponseDTO crearBoleta(BoletaRequestDTO request);
    BoletaResponseDTO obtenerBoleta(Long id);
    List<BoletaResponseDTO> listarBoletas();
     BoletaResponseDTO actualizarBoleta(Long id, BoletaRequestDTO request);
    void eliminarBoleta(Long id);
}
