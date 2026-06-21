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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoResponseDto crear(ProductoRequestDto dto) {
        log.info("Iniciando creación de producto con SKU: {}", dto.getSku());

        if (productoRepository.existsBySku(dto.getSku())) {
            log.warn("Intento de crear producto con SKU duplicado: {}", dto.getSku());
            throw new DuplicateResourceException("Ya existe un producto con ese SKU");
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setSku(dto.getSku());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());

        producto = productoRepository.save(producto);

        log.info("Producto creado correctamente con id: {}", producto.getId());
        return mapToResponse(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listar() {
        log.debug("Listando todos los productos");
        return productoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto obtenerPorId(Long id) {
        log.debug("Buscando producto con id: {}", id);
        Producto producto = getProductoOrThrow(id);
        return mapToResponse(producto);
    }

    @Override
    public ProductoResponseDto actualizar(Long id, ProductoUpdateDto dto) {
        log.info("Actualizando producto con id: {}", id);

        Producto producto = getProductoOrThrow(id);

        if (productoRepository.existsBySkuAndIdNot(dto.getSku(), id)) {
            log.warn("Intento de actualizar producto {} con SKU duplicado: {}", id, dto.getSku());
            throw new DuplicateResourceException("Ya existe otro producto con ese SKU");
        }

        producto.setNombre(dto.getNombre());
        producto.setSku(dto.getSku());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());

        producto = productoRepository.save(producto);

        log.info("Producto actualizado correctamente con id: {}", id);
        return mapToResponse(producto);
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

    private ProductoResponseDto mapToResponse(Producto producto) {
        return new ProductoResponseDto(
                producto.getId(),
                producto.getNombre(),
                producto.getSku(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getActivo()
        );
    }
}