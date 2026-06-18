package com.example.ms_producto.service.impl;

import com.example.ms_producto.dto.MessageResponseDto;
import com.example.ms_producto.dto.ProductoRequestDto;
import com.example.ms_producto.dto.ProductoResponseDto;
import com.example.ms_producto.dto.ProductoUpdateDto;
import com.example.ms_producto.exception.DuplicateResourceException;
import com.example.ms_producto.exception.ResourceNotFoundException;
import com.example.ms_producto.model.Producto;
import com.example.ms_producto.repository.ProductoRepository;
import com.example.ms_producto.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProductoServiceImpl.class)
@ActiveProfiles("test")
public class ProductoServiceImplTest {

    @Autowired
    private ProductoService productoService;

    @MockitoBean
    private ProductoRepository productoRepository;

    @Test
    public void testCrear() {
        ProductoRequestDto dto = new ProductoRequestDto();
        dto.setNombre("Notebook Gamer");
        dto.setSku("SKU-001");
        dto.setPrecio(new BigDecimal("899990"));
        dto.setStock(10);
        dto.setActivo(true);

        when(productoRepository.existsBySku("SKU-001")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto producto = invocation.getArgument(0);
            producto.setId(1L);
            return producto;
        });

        ProductoResponseDto resultado = productoService.crear(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Notebook Gamer", resultado.getNombre());
        assertEquals("SKU-001", resultado.getSku());
        assertEquals(new BigDecimal("899990"), resultado.getPrecio());
        assertEquals(10, resultado.getStock());
        assertTrue(resultado.getActivo());
    }

    @Test
    public void testCrearDuplicado() {
        ProductoRequestDto dto = new ProductoRequestDto();
        dto.setNombre("Notebook Gamer");
        dto.setSku("SKU-001");
        dto.setPrecio(new BigDecimal("899990"));
        dto.setStock(10);
        dto.setActivo(true);

        when(productoRepository.existsBySku("SKU-001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> productoService.crear(dto));
    }

    @Test
    public void testListar() {
        Producto producto = crearProducto(1L, "Notebook Gamer", "SKU-001", new BigDecimal("899990"), 10, true);

        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponseDto> resultado = productoService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Notebook Gamer", resultado.get(0).getNombre());
        assertEquals("SKU-001", resultado.get(0).getSku());
    }

    @Test
    public void testObtenerPorId() {
        Producto producto = crearProducto(1L, "Notebook Gamer", "SKU-001", new BigDecimal("899990"), 10, true);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoResponseDto resultado = productoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Notebook Gamer", resultado.getNombre());
        assertEquals("SKU-001", resultado.getSku());
    }

    @Test
    public void testObtenerPorIdNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.obtenerPorId(1L));
    }

    @Test
    public void testActualizar() {
        Producto producto = crearProducto(1L, "Notebook Gamer", "SKU-001", new BigDecimal("899990"), 10, true);

        ProductoUpdateDto dto = new ProductoUpdateDto();
        dto.setNombre("Notebook Gamer Pro");
        dto.setSku("SKU-002");
        dto.setPrecio(new BigDecimal("999990"));
        dto.setStock(8);
        dto.setActivo(true);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.existsBySkuAndIdNot("SKU-002", 1L)).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponseDto resultado = productoService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals("Notebook Gamer Pro", resultado.getNombre());
        assertEquals("SKU-002", resultado.getSku());
        assertEquals(new BigDecimal("999990"), resultado.getPrecio());
        assertEquals(8, resultado.getStock());
        assertTrue(resultado.getActivo());
    }

    @Test
    public void testActualizarDuplicado() {
        Producto producto = crearProducto(1L, "Notebook Gamer", "SKU-001", new BigDecimal("899990"), 10, true);

        ProductoUpdateDto dto = new ProductoUpdateDto();
        dto.setNombre("Notebook Gamer Pro");
        dto.setSku("SKU-002");
        dto.setPrecio(new BigDecimal("999990"));
        dto.setStock(8);
        dto.setActivo(true);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.existsBySkuAndIdNot("SKU-002", 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> productoService.actualizar(1L, dto));
    }

    @Test
    public void testEliminar() {
        Producto producto = crearProducto(1L, "Notebook Gamer", "SKU-001", new BigDecimal("899990"), 10, true);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        MessageResponseDto resultado = productoService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Producto eliminado correctamente", resultado.getMensaje());
        verify(productoRepository, times(1)).delete(producto);
    }

    @Test
    public void testEliminarNoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.eliminar(1L));
    }

    private Producto crearProducto(Long id, String nombre, String sku, BigDecimal precio, Integer stock, Boolean activo) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setSku(sku);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setActivo(activo);
        return producto;
    }
}