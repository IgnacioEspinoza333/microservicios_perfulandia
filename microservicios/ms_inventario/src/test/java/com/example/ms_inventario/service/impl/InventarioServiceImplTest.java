package com.example.ms_inventario.service.impl;

import com.example.ms_inventario.dto.*;
import com.example.ms_inventario.exception.DuplicateResourceException;
import com.example.ms_inventario.exception.ResourceNotFoundException;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;
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
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository repository;

    @InjectMocks
    private InventarioServiceImpl service;

    @Test
    void debeCrearInventario() {
        InventarioRequestDto dto = new InventarioRequestDto();
        dto.setProductoId(1L);
        dto.setStock(10);

        when(repository.existsByProductoId(1L)).thenReturn(false);
        when(repository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        InventarioResponseDto response = service.crear(dto);

        assertEquals(1L, response.getProductoId());
        assertEquals(10, response.getStock());
    }

    @Test
    void debeFallarSiInventarioDuplicado() {
        InventarioRequestDto dto = new InventarioRequestDto();
        dto.setProductoId(1L);

        when(repository.existsByProductoId(1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.crear(dto));
    }

    @Test
    void debeListarInventarios() {
        Inventario inv = new Inventario();
        inv.setId(1L);

        when(repository.findAll()).thenReturn(List.of(inv));

        List<InventarioResponseDto> lista = service.listar();

        assertEquals(1, lista.size());
    }

    @Test
    void debeObtenerPorId() {
        Inventario inv = new Inventario();
        inv.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(inv));

        InventarioResponseDto response = service.obtenerPorId(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void debeLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.obtenerPorId(1L));
    }

    @Test
    void debeObtenerPorProductoId() {
        Inventario inv = new Inventario();
        inv.setProductoId(1L);

        when(repository.findByProductoId(1L)).thenReturn(Optional.of(inv));

        InventarioResponseDto response =
                service.obtenerPorProductoId(1L);

        assertEquals(1L, response.getProductoId());
    }

    @Test
    void debeActualizarInventario() {
        Inventario inv = new Inventario();
        inv.setId(1L);

        InventarioUpdateDto dto = new InventarioUpdateDto();
        dto.setProductoId(1L);
        dto.setStock(20);

        when(repository.findById(1L)).thenReturn(Optional.of(inv));
        when(repository.existsByProductoIdAndIdNot(1L, 1L)).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InventarioResponseDto response = service.actualizar(1L, dto);

        assertEquals(20, response.getStock());
    }

    @Test
    void debeEliminarInventario() {
        Inventario inv = new Inventario();
        inv.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(inv));

        MessageResponseDto msg = service.eliminar(1L);

        verify(repository).delete(inv);
        assertEquals("Inventario eliminado correctamente", msg.getMensaje());
    }
}