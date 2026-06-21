package com.example.ms_clientes.service.impl;

import com.example.ms_clientes.dto.ClienteRequestDto;
import com.example.ms_clientes.dto.ClienteResponseDto;
import com.example.ms_clientes.exception.DuplicateResourceException;
import com.example.ms_clientes.repository.ClienteRepository;
import com.example.ms_clientes.repository.DireccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repo;

    @Mock
    private DireccionRepository dirRepo;

    @InjectMocks
    private ClienteServiceImpl service;

    @Test
    void debeCrearCliente() {
        ClienteRequestDto dto = new ClienteRequestDto();
        dto.setEmail("test@mail.com");

        when(repo.existsByEmail(any())).thenReturn(false);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        ClienteResponseDto res = service.crear(dto);

        assertEquals("test@mail.com", res.getEmail());
    }

    @Test
    void debeFallarPorEmailDuplicado() {
        when(repo.existsByEmail(any())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.crear(new ClienteRequestDto()));
    }
}