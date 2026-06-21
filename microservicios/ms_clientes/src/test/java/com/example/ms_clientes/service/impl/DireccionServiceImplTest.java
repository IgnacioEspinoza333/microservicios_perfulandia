package com.example.ms_clientes.service.impl;

import com.example.ms_clientes.dto.DireccionRequestDto;
import com.example.ms_clientes.dto.DireccionResponseDto;
import com.example.ms_clientes.model.Cliente;
import com.example.ms_clientes.repository.ClienteRepository;
import com.example.ms_clientes.repository.DireccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DireccionServiceImplTest {

    @Mock
    private DireccionRepository dirRepo;

    @Mock
    private ClienteRepository cliRepo;

    @InjectMocks
    private DireccionServiceImpl service;

    @Test
    void debeCrearDireccion() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        DireccionRequestDto dto = new DireccionRequestDto();
        dto.setCalle("Calle");

        when(cliRepo.findById(1L)).thenReturn(Optional.of(cliente));
        when(dirRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        DireccionResponseDto res = service.crear(1L, dto);

        assertEquals("Calle", res.getCalle());
    }
}