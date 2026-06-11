package com.example.ms_boleta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ms_boleta.dtos.BoletaRequestDTO;
import com.example.ms_boleta.dtos.BoletaResponseDTO;
import com.example.ms_boleta.exceptions.DuplicateResourceException;
import com.example.ms_boleta.exceptions.ResourceNotFoundException;
import com.example.ms_boleta.modelo.Boleta;
import com.example.ms_boleta.repository.BoletaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoletaServiceImpl implements BoletaService {
    private final BoletaRepository boletaRepository;

    @Override
    public BoletaResponseDTO crearBoleta(BoletaRequestDTO request) {
        if (boletaRepository.existsByNumero(request.getNumero())) {
            throw new DuplicateResourceException("La boleta con número " + request.getNumero() + " ya existe.");
        }
        Boleta boleta = Boleta.builder()
                .numero(request.getNumero())
                .cliente(request.getCliente())
                .monto(request.getMonto())
                .fechaEmision(LocalDateTime.now())
                .build();
        return toResponse(boletaRepository.save(boleta));
    }

    @Override
    public BoletaResponseDTO obtenerBoleta(Long id) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta no encontrada con id: " + id));
        return toResponse(boleta);
    }

    @Override
    public List<BoletaResponseDTO> listarBoletas() {
        return boletaRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void eliminarBoleta(Long id) {
        if (!boletaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Boleta no encontrada con id: " + id);
        }
        boletaRepository.deleteById(id);
    }

    private BoletaResponseDTO toResponse(Boleta boleta) {
        return BoletaResponseDTO.builder()
                .id(boleta.getId())
                .numero(boleta.getNumero())
                .cliente(boleta.getCliente())
                .monto(boleta.getMonto())
                .fechaEmision(boleta.getFechaEmision())
                .build();
    }
}
