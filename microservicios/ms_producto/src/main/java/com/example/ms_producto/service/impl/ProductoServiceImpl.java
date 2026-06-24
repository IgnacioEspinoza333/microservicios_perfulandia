package com.example.ms_producto.service.impl;

import com.example.ms_producto.client.CategoriaClient;
import com.example.ms_producto.client.ProveedorClient;
import com.example.ms_producto.dto.*;
import com.example.ms_producto.exception.BusinessException;
import com.example.ms_producto.exception.DuplicateResourceException;
import com.example.ms_producto.exception.ResourceNotFoundException;
import com.example.ms_producto.model.Producto;
import com.example.ms_producto.repository.ProductoRepository;
import com.example.ms_producto.service.ProductoService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaClient categoriaClient;
    private final ProveedorClient proveedorClient;

    @Value("${producto.enriquecer.proveedor-en-listado:false}")
    private boolean enriquecerProveedorEnListado;

    @Override
    public ProductoResponseDto crear(ProductoRequestDto dto) {
        log.info("Iniciando creación de producto con SKU: {}", dto.getSku());

        if (productoRepository.existsBySku(dto.getSku())) {
            log.warn("Intento de crear producto con SKU duplicado: {}", dto.getSku());
            throw new DuplicateResourceException("Ya existe un producto con ese SKU");
        }

        CategoriaExternaDto categoria = obtenerCategoriaOrThrow(dto.getCategoriaId());
        ProveedorExternoDto proveedor = obtenerProveedorOrThrow(dto.getProveedorId());

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setSku(dto.getSku());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());
        producto.setCategoriaId(dto.getCategoriaId());
        producto.setProveedorId(dto.getProveedorId());

        producto = productoRepository.save(producto);

        log.info("Producto creado correctamente con id: {}", producto.getId());
        return mapToResponse(producto, categoria, proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listar() {
        log.debug("Listando todos los productos");

        List<Producto> productos = productoRepository.findAll();

        Map<Long, CategoriaExternaDto> categoriaCache = new HashMap<>();
        Map<Long, ProveedorExternoDto> proveedorCache = new HashMap<>();

        return productos.stream()
                .map(producto -> {
                    CategoriaExternaDto categoria = categoriaCache.computeIfAbsent(
                            producto.getCategoriaId(),
                            this::obtenerCategoriaOrThrow
                    );

                    ProveedorExternoDto proveedor = null;
                    if (enriquecerProveedorEnListado) {
                        proveedor = proveedorCache.computeIfAbsent(
                                producto.getProveedorId(),
                                this::obtenerProveedorOrThrow
                        );
                    }

                    return mapToResponse(producto, categoria, proveedor);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto obtenerPorId(Long id) {
        log.debug("Buscando producto con id: {}", id);

        Producto producto = getProductoOrThrow(id);

        CategoriaExternaDto categoria = obtenerCategoriaOrThrow(producto.getCategoriaId());
        ProveedorExternoDto proveedor = obtenerProveedorOrThrow(producto.getProveedorId());

        return mapToResponse(producto, categoria, proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResumenDto obtenerResumenPorId(Long id) {
        log.debug("Buscando resumen de producto con id: {}", id);

        Producto producto = getProductoOrThrow(id);

        return new ProductoResumenDto(
                producto.getId(),
                producto.getNombre(),
                producto.getSku(),
                producto.getActivo()
        );
    }


    @Override
    public ProductoResponseDto actualizar(Long id, ProductoUpdateDto dto) {
        log.info("Actualizando producto con id: {}", id);

        Producto producto = getProductoOrThrow(id);

        if (productoRepository.existsBySkuAndIdNot(dto.getSku(), id)) {
            log.warn("Intento de actualizar producto {} con SKU duplicado: {}", id, dto.getSku());
            throw new DuplicateResourceException("Ya existe otro producto con ese SKU");
        }

        CategoriaExternaDto categoria = obtenerCategoriaOrThrow(dto.getCategoriaId());
        ProveedorExternoDto proveedor = obtenerProveedorOrThrow(dto.getProveedorId());

        producto.setNombre(dto.getNombre());
        producto.setSku(dto.getSku());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());
        producto.setCategoriaId(dto.getCategoriaId());
        producto.setProveedorId(dto.getProveedorId());

        producto = productoRepository.save(producto);

        log.info("Producto actualizado correctamente con id: {}", id);
        return mapToResponse(producto, categoria, proveedor);
    }

    @Override
    public MessageResponseDto eliminar(Long id) {
        log.warn("Eliminando producto con id: {}", id);

        Producto producto = getProductoOrThrow(id);
        productoRepository.delete(producto);

        log.info("Producto eliminado correctamente con id: {}", id);
        return new MessageResponseDto("Producto eliminado correctamente");
    }

    private Producto getProductoOrThrow(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado con id: " + id);
                });
    }

    private CategoriaExternaDto obtenerCategoriaOrThrow(Long categoriaId) {
        try {
            return categoriaClient.obtenerPorId(categoriaId);

        } catch (FeignException.NotFound ex) {
            log.warn("Categoría no encontrada en ms_categoria. categoriaId={}", categoriaId);
            throw new BusinessException("La categoría con id " + categoriaId + " no existe");

        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.error("Error de autenticación/autorización consultando ms_categoria");
            throw new BusinessException("No fue posible autenticarse contra ms_categoria");

        } catch (FeignException ex) {
            log.error("Error consultando ms_categoria. status={}, body={}", ex.status(), ex.contentUTF8());
            throw new BusinessException("Error al consultar ms_categoria");
        }
    }

    private ProveedorExternoDto obtenerProveedorOrThrow(Long proveedorId) {
        try {
            return proveedorClient.obtenerPorId(proveedorId);

        } catch (FeignException.NotFound ex) {
            log.warn("Proveedor no encontrado en ms_proveedores. proveedorId={}", proveedorId);
            throw new BusinessException("El proveedor con id " + proveedorId + " no existe");

        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.error("Error de autenticación/autorización consultando ms_proveedores");
            throw new BusinessException("No fue posible autenticarse contra ms_proveedores");

        } catch (FeignException ex) {
            log.error("Error consultando ms_proveedores. status={}, body={}", ex.status(), ex.contentUTF8());
            throw new BusinessException("Error al consultar ms_proveedores");
        }
    }

    private ProductoResponseDto mapToResponse(
            Producto producto,
            CategoriaExternaDto categoria,
            ProveedorExternoDto proveedor
    ) {
        return new ProductoResponseDto(
                producto.getId(),
                producto.getNombre(),
                producto.getSku(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getActivo(),
                producto.getCategoriaId(),
                producto.getProveedorId(),
                categoria,
                proveedor
        );
    }
}