package com.example.ms_envio.service;

import java.util.List;

import com.example.ms_envio.dto.EnvioRequestDTO;
import com.example.ms_envio.dto.EnvioResponseDTO;

public interface EnvioService {
    EnvioResponseDTO crearEnvio(EnvioRequestDTO request);
    EnvioResponseDTO obtenerEnvio(Long id);
    List<EnvioResponseDTO> listarEnvios();
    void cancelarEnvio(Long id);
    EnvioResponseDTO actualizarEnvio(Long id, EnvioRequestDTO request);
    void eliminarEnvio(Long id); // <-- faltaba este
}
