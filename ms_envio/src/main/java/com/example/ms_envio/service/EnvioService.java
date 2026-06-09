package com.example.ms_envio.service;

import java.util.List;

import com.example.ms_envio.dtos.EnvioRequestDTO;
import com.example.ms_envio.dtos.EnvioResponseDTO;

public interface EnvioService {
     EnvioResponseDTO crearEnvio(EnvioRequestDTO request);
    EnvioResponseDTO obtenerEnvio(Long id);
    List<EnvioResponseDTO> listarEnvios();
    void cancelarEnvio(Long id);
}
