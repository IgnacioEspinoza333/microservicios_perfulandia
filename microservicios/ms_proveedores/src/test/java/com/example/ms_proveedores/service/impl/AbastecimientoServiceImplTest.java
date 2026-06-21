package com.example.ms_proveedores.service.impl;

import com.example.ms_proveedores.dto.AbastecimientoRequestDto;
import com.example.ms_proveedores.dto.AbastecimientoResponseDto;
import com.example.ms_proveedores.dto.AbastecimientoUpdateDto;
import com.example.ms_proveedores.dto.MessageResponseDto;
import com.example.ms_proveedores.exception.BusinessException;
import com.example.ms_proveedores.exception.ResourceNotFoundException;
import com.example.ms_proveedores.model.Abastecimiento;
import com.example.ms_proveedores.model.Proveedor;
import com.example.ms_proveedores.repository.AbastecimientoRepository;
import com.example.ms_proveedores.repository.ProveedorRepository;
import com.example.ms_proveedores.service.AbastecimientoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AbastecimientoServiceImpl.class)
@ActiveProfiles("test")
public class AbastecimientoServiceImplTest {

    @Autowired
    private AbastecimientoService abastecimientoService;

    @MockitoBean
    private AbastecimientoRepository abastecimientoRepository;

    @MockitoBean
    private ProveedorRepository proveedorRepository;

    @Test
    public void testCrear() {
        Proveedor proveedor = crearProveedor();
        AbastecimientoRequestDto dto = new AbastecimientoRequestDto();
        dto.setProveedorId(1L);
        dto.setProductoId(100L);
        dto.setCantidad(50);
        dto.setEstado("PENDIENTE");

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(abastecimientoRepository.save(any(Abastecimiento.class))).thenAnswer(invocation -> {
            Abastecimiento a = invocation.getArgument(0);
            a.setId(1L);
            a.setVersion(1L);
            return a;
        });

        AbastecimientoResponseDto resultado = abastecimientoService.crear(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getProveedorId());
        assertEquals("Proveedor Uno", resultado.getNombreProveedor());
        assertEquals(100L, resultado.getProductoId());
        assertEquals(50, resultado.getCantidad());
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    public void testCrearConEstadoNullDebePonerPendiente() {
        Proveedor proveedor = crearProveedor();

        AbastecimientoRequestDto dto = new AbastecimientoRequestDto();
        dto.setProveedorId(1L);
        dto.setProductoId(100L);
        dto.setCantidad(50);
        dto.setEstado(null);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(abastecimientoRepository.save(any(Abastecimiento.class))).thenAnswer(invocation -> {
            Abastecimiento a = invocation.getArgument(0);
            a.setId(1L);
            a.setVersion(1L);
            return a;
        });

        AbastecimientoResponseDto resultado = abastecimientoService.crear(dto);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    public void testCrearProveedorNoExiste() {
        AbastecimientoRequestDto dto = new AbastecimientoRequestDto();
        dto.setProveedorId(1L);
        dto.setProductoId(100L);
        dto.setCantidad(50);
        dto.setEstado("PENDIENTE");

        when(proveedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> abastecimientoService.crear(dto));
    }

    @Test
    public void testCrearEstadoInvalido() {
        Proveedor proveedor = crearProveedor();

        AbastecimientoRequestDto dto = new AbastecimientoRequestDto();
        dto.setProveedorId(1L);
        dto.setProductoId(100L);
        dto.setCantidad(50);
        dto.setEstado("INVALIDO");

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        assertThrows(BusinessException.class, () -> abastecimientoService.crear(dto));
    }

    @Test
    public void testListar() {
        Proveedor proveedor = crearProveedor();
        Abastecimiento abastecimiento = crearAbastecimiento();

        when(abastecimientoRepository.findAll()).thenReturn(List.of(abastecimiento));
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        List<AbastecimientoResponseDto> resultado = abastecimientoService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Proveedor Uno", resultado.get(0).getNombreProveedor());
    }

    @Test
    public void testListarPorProveedor() {
        Proveedor proveedor = crearProveedor();
        Abastecimiento abastecimiento = crearAbastecimiento();

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(abastecimientoRepository.findByProveedorId(1L)).thenReturn(List.of(abastecimiento));

        List<AbastecimientoResponseDto> resultado = abastecimientoService.listarPorProveedor(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getProveedorId());
    }

    @Test
    public void testListarPorProducto() {
        Proveedor proveedor = crearProveedor();
        Abastecimiento abastecimiento = crearAbastecimiento();

        when(abastecimientoRepository.findByProductoId(100L)).thenReturn(List.of(abastecimiento));
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        List<AbastecimientoResponseDto> resultado = abastecimientoService.listarPorProducto(100L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100L, resultado.get(0).getProductoId());
    }

    @Test
    public void testObtenerPorId() {
        Proveedor proveedor = crearProveedor();
        Abastecimiento abastecimiento = crearAbastecimiento();

        when(abastecimientoRepository.findById(1L)).thenReturn(Optional.of(abastecimiento));
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        AbastecimientoResponseDto resultado = abastecimientoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Proveedor Uno", resultado.getNombreProveedor());
    }

    @Test
    public void testActualizar() {
        Proveedor proveedor = crearProveedor();
        Abastecimiento abastecimiento = crearAbastecimiento();

        AbastecimientoUpdateDto dto = new AbastecimientoUpdateDto();
        dto.setProveedorId(1L);
        dto.setNombreProveedor("Proveedor Actualizado");
        dto.setProductoId(100L);
        dto.setCantidad(80);
        dto.setEstado("COMPLETADO");

        when(abastecimientoRepository.findById(1L)).thenReturn(Optional.of(abastecimiento));
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(abastecimientoRepository.save(any(Abastecimiento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AbastecimientoResponseDto resultado = abastecimientoService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals(80, resultado.getCantidad());
        assertEquals("COMPLETADO", resultado.getEstado());
        assertEquals("Proveedor Actualizado", resultado.getNombreProveedor());
    }

    @Test
    public void testEliminar() {
        Abastecimiento abastecimiento = crearAbastecimiento();

        when(abastecimientoRepository.findById(1L)).thenReturn(Optional.of(abastecimiento));

        MessageResponseDto resultado = abastecimientoService.eliminar(1L);

        assertNotNull(resultado);
        assertEquals("Abastecimiento eliminado correctamente", resultado.getMensaje());

        verify(abastecimientoRepository, times(1)).delete(abastecimiento);
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

    private Abastecimiento crearAbastecimiento() {
        Abastecimiento abastecimiento = new Abastecimiento();
        abastecimiento.setId(1L);
        abastecimiento.setProveedorId(1L);
        abastecimiento.setProductoId(100L);
        abastecimiento.setCantidad(50);
        abastecimiento.setEstado("PENDIENTE");
        abastecimiento.setFechaCreacion(Instant.now());
        abastecimiento.setVersion(1L);
        return abastecimiento;
    }
}