package com.example.ms_pago.service.impl;

import com.example.ms_pago.dto.PagoRequestDTO;
import com.example.ms_pago.dto.PagoResponseDTO;
import com.example.ms_pago.exception.PagoNotFoundException;
import com.example.ms_pago.model.Pago;
import com.example.ms_pago.repository.PagoRepository;
import com.example.ms_pago.service.PagoServiceImpl;

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
class PagoServiceImplTest {

    @Mock
    private PagoRepository repository;

    @InjectMocks
    private PagoServiceImpl service;

    @Test
    void debeCrearPago() {
        PagoRequestDTO dto = new PagoRequestDTO();
        dto.setMonto(100.0);
        dto.setMetodo("Tarjeta");

        when(repository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        PagoResponseDTO response = service.crearPago(dto);

        assertEquals(100.0, response.getMonto());
        assertEquals("Tarjeta", response.getMetodo());
        assertEquals("PENDIENTE", response.getEstado());
        assertNotNull(response.getFecha());
    }

    @Test
    void debeObtenerPago() {
        Pago pago = Pago.builder()
                .id(1L)
                .monto(100.0)
                .metodo("Tarjeta")
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(pago));

        PagoResponseDTO response = service.obtenerPago(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void debeLanzarExcepcionSiNoExistePago() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PagoNotFoundException.class,
                () -> service.obtenerPago(1L));
    }

    @Test
    void debeListarPagos() {
        Pago pago = new Pago();
        pago.setId(1L);

        when(repository.findAll()).thenReturn(List.of(pago));

        List<PagoResponseDTO> lista = service.listarPagos();

        assertEquals(1, lista.size());
    }

    @Test
    void debeEliminarPago() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarPago(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void debeFallarEliminarSiNoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(PagoNotFoundException.class,
                () -> service.eliminarPago(1L));
    }
}