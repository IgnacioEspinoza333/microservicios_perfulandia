package com.example.ms_proveedores.service.impl;

import com.example.ms_proveedores.dto.MessageResponseDto;
import com.example.ms_proveedores.dto.ProveedorRequestDto;
import com.example.ms_proveedores.dto.ProveedorResponseDto;
import com.example.ms_proveedores.dto.ProveedorUpdateDto;
import com.example.ms_proveedores.exception.DuplicateResourceException;
import com.example.ms_proveedores.exception.ResourceNotFoundException;
import com.example.ms_proveedores.model.Proveedor;
import com.example.ms_proveedores.repository.AbastecimientoRepository;
import com.example.ms_proveedores.repository.ProveedorRepository;
import com.example.ms_proveedores.service.ProveedorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProveedorServiceImpl.class)
@ActiveProfiles("test")
public class ProveedorServiceImplTest {

    @Autowired
    private ProveedorService proveedorService;

    @MockitoBean
    private ProveedorRepository proveedorRepository;

    @MockitoBean
    private AbastecimientoRepository abastecimientoRepository;

    @Test
    public void testCrear() {
        ProveedorRequestDto dto = new ProveedorRequestDto();
        dto.setNombre("Proveedor Uno");
        dto.setEmail("proveedor1@test.com");
        dto.setTelefono("987654321");
        dto.setDireccion("Calle 123");
        dto.setActivo(true);

        when(proveedorRepository.existsByEmail("proveedor1@test.com")).thenReturn(false);
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(invocation -> {
            Proveedor proveedor = invocation.getArgument(0);
            proveedor.setId(1L);
            proveedor.setVersion(1L);
            return proveedor;
        });

        ProveedorResponseDto resultado = proveedorService.crear(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Proveedor Uno", resultado.getNombre());
        assertEquals("proveedor1@test.com", resultado.getEmail());
        assertTrue(resultado.getActivo());
    }

    @Test
    public void testCrearDuplicado() {
        ProveedorRequestDto dto = new ProveedorRequestDto();
        dto.setNombre("Proveedor Uno");
        dto.setEmail("proveedor1@test.com");
        dto.setTelefono("987654321");
        dto.setDireccion("Calle 123");
        dto.setActivo(true);

        when(proveedorRepository.existsByEmail("proveedor1@test.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> proveedorService.crear(dto));
    }

    @Test
    public void testListar() {
        Proveedor proveedor = crearProveedor();

        when(proveedorRepository.findAll()).thenReturn(List.of(proveedor));

        List<ProveedorResponseDto> resultado = proveedorService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Proveedor Uno", resultado.get(0).getNombre());
        assertEquals("proveedor1@test.com", resultado.get(0).getEmail());
    }

    @Test
    public void testObtenerPorId() {
        Proveedor proveedor = crearProveedor();

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        ProveedorResponseDto resultado = proveedorService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Proveedor Uno", resultado.getNombre());
    }

    @Test
    public void testObtenerPorIdNoExiste() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> proveedorService.obtenerPorId(1L));
    }

    @Test
    public void testActualizar() {
        Proveedor proveedor = crearProveedor();

        ProveedorUpdateDto dto = new ProveedorUpdateDto();
        dto.setNombre("Proveedor Uno Actualizado");
        dto.setEmail("proveedor1updated@test.com");
        dto.setTelefono("999999999");
        dto.setDireccion("Nueva dirección 456");
        dto.setActivo(false);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.existsByEmailAndIdNot("proveedor1updated@test.com", 1L)).thenReturn(false);
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProveedorResponseDto resultado = proveedorService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals("Proveedor Uno Actualizado", resultado.getNombre());
        assertEquals("proveedor1updated@test.com", resultado.getEmail());
        assertFalse(resultado.getActivo());
    }

    @Test
    public void testActualizarDuplicado() {
        Proveedor proveedor = crearProveedor();

        ProveedorUpdateDto dto = new ProveedorUpdateDto();
        dto.setNombre("Proveedor Uno Actualizado");
        dto.setEmail("proveedor1updated@test.com");
        dto.setTelefono("999999999");
        dto.setDireccion("Nueva dirección 456");
        dto.setActivo(false);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.existsByEmailAndIdNot("proveedor1updated@test.com", 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> proveedorService.actualizar(1L, dto));
    }

    @Test
    public void testEliminarConAbastecimientos() {
        Proveedor proveedor = crearProveedor();

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(abastecimientoRepository.existsByProveedorId(1L)).thenReturn(true);

        MessageResponseDto resultado = proveedorService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Proveedor eliminado correctamente", resultado.getMensaje());

        verify(abastecimientoRepository, times(1)).deleteByProveedorId(1L);
        verify(proveedorRepository, times(1)).delete(proveedor);
    }

    @Test
    public void testEliminarSinAbastecimientos() {
        Proveedor proveedor = crearProveedor();

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(abastecimientoRepository.existsByProveedorId(1L)).thenReturn(false);

        MessageResponseDto resultado = proveedorService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Proveedor eliminado correctamente", resultado.getMensaje());

        verify(abastecimientoRepository, never()).deleteByProveedorId(1L);
        verify(proveedorRepository, times(1)).delete(proveedor);
    }

    private Proveedor crearProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Proveedor Uno");
        proveedor.setEmail("proveedor1@test.com");
        proveedor.setTelefono("987654321");
        proveedor.setDireccion("Calle 123");
        proveedor.setActivo(true);
        proveedor.setVersion(1L);
        return proveedor;
    }
}