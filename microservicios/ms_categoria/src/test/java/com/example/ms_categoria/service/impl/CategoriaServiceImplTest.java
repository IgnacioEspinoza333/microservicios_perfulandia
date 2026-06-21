package com.example.ms_categoria.service.impl;

import com.example.ms_categoria.dto.CategoriaRequestDTO;
import com.example.ms_categoria.dto.CategoriaResponseDTO;
import com.example.ms_categoria.exception.DuplicateResourceException;
import com.example.ms_categoria.exception.ResourceNotFoundException;
import com.example.ms_categoria.model.Categoria;
import com.example.ms_categoria.repository.CategoriaRepository;
import com.example.ms_categoria.service.CategoriaServiceImpl;

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
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaServiceImpl service;

    @Test
    void debeCrearCategoria() {
        CategoriaRequestDTO dto = CategoriaRequestDTO.builder()
                .nombre("Electrónica")
                .descripcion("Desc")
                .build();

        when(repository.existsByNombre("Electrónica")).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        CategoriaResponseDTO response = service.crearCategoria(dto);

        assertEquals("Electrónica", response.getNombre());
    }

    @Test
    void debeFallarPorDuplicado() {
        when(repository.existsByNombre("Electrónica")).thenReturn(true);

        CategoriaRequestDTO dto = CategoriaRequestDTO.builder()
                .nombre("Electrónica")
                .descripcion("Desc")
                .build();

        assertThrows(DuplicateResourceException.class,
                () -> service.crearCategoria(dto));
    }

    @Test
    void debeObtenerCategoria() {
        Categoria categoria = new Categoria(1L, "Cat", "Desc");

        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaResponseDTO res = service.obtenerCategoria(1L);

        assertEquals(1L, res.getId());
    }

    @Test
    void debeLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.obtenerCategoria(1L));
    }

    @Test
    void debeListarCategorias() {
        when(repository.findAll()).thenReturn(List.of(new Categoria()));

        List<CategoriaResponseDTO> lista = service.listarCategorias();

        assertEquals(1, lista.size());
    }

    @Test
    void debeEliminarCategoria() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarCategoria(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void debeFallarEliminarSiNoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> service.eliminarCategoria(1L));
    }

    @Test
    void debeActualizarCategoria() {
        Categoria cat = new Categoria(1L, "Old", "Old");

        CategoriaRequestDTO dto = CategoriaRequestDTO.builder()
                .nombre("New")
                .descripcion("NewDesc")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(cat));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        CategoriaResponseDTO res = service.actualizarCategoria(1L, dto);

        assertEquals("New", res.getNombre());
    }
}