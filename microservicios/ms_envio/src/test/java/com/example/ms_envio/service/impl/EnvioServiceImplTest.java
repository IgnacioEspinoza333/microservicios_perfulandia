package com.example.ms_envio.service.impl;

import com.example.ms_envio.dto.EnvioRequestDTO;
import com.example.ms_envio.dto.EnvioResponseDTO;
import com.example.ms_envio.exception.EnvioNotFoundException;
import com.example.ms_envio.model.Envio;
import com.example.ms_envio.model.EstadoEnvio;
import com.example.ms_envio.repository.EnvioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceImplTest {

    @Mock
    private EnvioRepository repository;

    @InjectMocks
    private EnvioServiceImpl service;

    @Test
    void debeCrearEnvio() {
        EnvioRequestDTO dto = new EnvioRequestDTO();
        dto.setDireccionDestino("Calle");
        dto.setCliente("Cliente");

        when(repository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        EnvioResponseDTO response = service.crearEnvio(dto);

        assertEquals("Calle", response.getDireccionDestino());
        assertEquals(EstadoEnvio.PENDIENTE, response.getEstado());
    }

    @Test
    void debeObtenerEnvio() {
        Envio envio = new Envio();
        envio.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(envio));

        EnvioResponseDTO response = service.obtenerEnvio(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void debeLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EnvioNotFoundException.class,
                () -> service.obtenerEnvio(1L));
    }

    @Test
    void debeListarEnvios() {
        Envio envio = new Envio();
        envio.setId(1L);

        when(repository.findAll()).thenReturn(List.of(envio));

        List<EnvioResponseDTO> lista = service.listarEnvios();

        assertEquals(1, lista.size());
    }

    @Test
    void debeCancelarEnvio() {
        Envio envio = new Envio();
        envio.setId(1L);
        envio.setEstado(EstadoEnvio.PENDIENTE);

        when(repository.findById(1L)).thenReturn(Optional.of(envio));

        service.cancelarEnvio(1L);

        assertEquals(EstadoEnvio.CANCELADO, envio.getEstado());
        verify(repository).save(envio);
    }

    @Test
    void debeActualizarEnvio() {
        Envio envio = new Envio();
        envio.setId(1L);

        EnvioRequestDTO dto = new EnvioRequestDTO();
        dto.setId(1L);
        dto.setDireccionDestino("Nueva");
        dto.setCliente("Cliente");
        dto.setFechaEnvio(LocalDateTime.now());
        dto.setEstado(EstadoEnvio.EN_CAMINO);

        when(repository.findById(1L)).thenReturn(Optional.of(envio));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        EnvioResponseDTO response = service.actualizarEnvio(1L, dto);

        assertEquals(EstadoEnvio.EN_CAMINO, response.getEstado());
    }

    @Test
    void debeFallarSiIdNoCoincide() {
        EnvioRequestDTO dto = new EnvioRequestDTO();
        dto.setId(2L);

        assertThrows(RuntimeException.class,
                () -> service.actualizarEnvio(1L, dto));
    }
}