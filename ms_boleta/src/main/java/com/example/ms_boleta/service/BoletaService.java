package com.example.ms_boleta.service;

import java.util.List;

import com.example.ms_boleta.dtos.BoletaRequestDTO;
import com.example.ms_boleta.dtos.BoletaResponseDTO;

public interface BoletaService {
    BoletaResponseDTO crearBoleta(BoletaRequestDTO request);
    BoletaResponseDTO obtenerBoleta(Long id);
    List<BoletaResponseDTO> listarBoletas();
    void eliminarBoleta(Long id);
}
