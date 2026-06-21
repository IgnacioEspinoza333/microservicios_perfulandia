package com.example.ms_boleta.service.impl;

import com.example.ms_boleta.dto.BoletaRequestDTO;
import com.example.ms_boleta.dto.BoletaResponseDTO;
import com.example.ms_boleta.exceptions.DuplicateResourceException;
import com.example.ms_boleta.exceptions.ResourceNotFoundException;
import com.example.ms_boleta.model.Boleta;
import com.example.ms_boleta.repository.BoletaRepository;
import com.example.ms_boleta.service.BoletaServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoletaServiceImplTest {

    @Mock
    private BoletaRepository repository;

    @InjectMocks
    private BoletaServiceImpl service;

    @Test
    void debeCrearBoleta() {
        BoletaRequestDTO dto = new BoletaRequestDTO();
        dto.setNumero("B001");

        when(repository.existsByNumero("B001")).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        BoletaResponseDTO response = service.crearBoleta(dto);

        assertEquals("B001", response.getNumero());
    }

    @Test
    void debeFallarPorDuplicado() {
        when(repository.existsByNumero("B001")).thenReturn(true);

        BoletaRequestDTO dto = new BoletaRequestDTO();
        dto.setNumero("B001");

        assertThrows(DuplicateResourceException.class,
                () -> service.crearBoleta(dto));
    }

    @Test
    void debeObtenerBoleta() {
        Boleta boleta = new Boleta();
        boleta.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(boleta));

        BoletaResponseDTO res = service.obtenerBoleta(1L);

        assertEquals(1L, res.getId());
    }

    @Test
    void debeLanzarErrorSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.obtenerBoleta(1L));
    }

    @Test
    void debeListarBoletas() {
        when(repository.findAll()).thenReturn(List.of(new Boleta()));

        List<BoletaResponseDTO> lista = service.listarBoletas();

        assertEquals(1, lista.size());
    }

    @Test
    void debeEliminarBoleta() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarBoleta(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void debeFallarEliminarSiNoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> service.eliminarBoleta(1L));
    }

    @Test
    void debeActualizarBoleta() {
        Boleta boleta = new Boleta();
        boleta.setId(1L);

        BoletaRequestDTO dto = new BoletaRequestDTO();
        dto.setNumero("B002");

        when(repository.findById(1L)).thenReturn(Optional.of(boleta));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        BoletaResponseDTO res = service.actualizarBoleta(1L, dto);

        assertEquals("B002", res.getNumero());
    }
}