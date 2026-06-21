package com.example.ms_envio.service.impl;


import org.springframework.stereotype.Service;

import com.example.ms_envio.dto.EnvioRequestDTO;
import com.example.ms_envio.dto.EnvioResponseDTO;
import com.example.ms_envio.exception.EnvioNotFoundException;
import com.example.ms_envio.model.Envio;
import com.example.ms_envio.model.EstadoEnvio;
import com.example.ms_envio.repository.EnvioRepository;
import com.example.ms_envio.service.EnvioService;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements EnvioService {
        private final EnvioRepository envioRepository;

    @Override
    public EnvioResponseDTO crearEnvio(EnvioRequestDTO request) {
        Envio envio = Envio.builder()
                .direccionDestino(request.getDireccionDestino())
                .cliente(request.getCliente())
                .fechaEnvio(LocalDateTime.now())
                .estado(EstadoEnvio.PENDIENTE)
                .build();
        return toResponse(envioRepository.save(envio));
    }

    @Override
    public EnvioResponseDTO obtenerEnvio(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNotFoundException("Envio no encontrado con id: " + id));
        return toResponse(envio);
    }

    @Override
    public List<EnvioResponseDTO> listarEnvios() {
        return envioRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void cancelarEnvio(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNotFoundException("Envio no encontrado con id: " + id));
        envio.setEstado(EstadoEnvio.CANCELADO);
        envioRepository.save(envio);
    }

    private EnvioResponseDTO toResponse(Envio envio) {
        return EnvioResponseDTO.builder()
                .id(envio.getId())
                .direccionDestino(envio.getDireccionDestino())
                .cliente(envio.getCliente())
                .fechaEnvio(envio.getFechaEnvio())
                .estado(envio.getEstado())
                .build();
    }
   @Override
public EnvioResponseDTO actualizarEnvio(Long id, EnvioRequestDTO request) {
    if (!id.equals(request.getId())) {
        throw new RuntimeException("El ID de la URL no coincide con el del cuerpo");
    }

    Envio envio = envioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

    envio.setDireccionDestino(request.getDireccionDestino());
    envio.setCliente(request.getCliente());
    envio.setFechaEnvio(request.getFechaEnvio());
    envio.setEstado(request.getEstado());

    Envio actualizado = envioRepository.save(envio);
    return new EnvioResponseDTO(actualizado.getId(), actualizado.getDireccionDestino(),
            actualizado.getCliente(), actualizado.getFechaEnvio(), actualizado.getEstado());
}
@Override
    public void eliminarEnvio(Long id) {
        if (!envioRepository.existsById(id)) {
            throw new RuntimeException("Envio no encontrado");
        }
        envioRepository.deleteById(id);
    }

}
