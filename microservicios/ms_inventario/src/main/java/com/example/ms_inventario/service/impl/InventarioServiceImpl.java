package com.example.ms_inventario.service.impl;

import com.example.ms_inventario.client.ProductoClient;
import com.example.ms_inventario.dto.InventarioRequestDto;
import com.example.ms_inventario.dto.InventarioResponseDto;
import com.example.ms_inventario.dto.InventarioUpdateDto;
import com.example.ms_inventario.dto.MessageResponseDto;
import com.example.ms_inventario.dto.ProductoResumenExternoDto;
import com.example.ms_inventario.exception.BusinessException;
import com.example.ms_inventario.exception.DuplicateResourceException;
import com.example.ms_inventario.exception.ResourceNotFoundException;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;
import com.example.ms_inventario.service.InventarioService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoClient productoClient;

    @Override
    public InventarioResponseDto crear(InventarioRequestDto dto) {
        log.info("Iniciando creación de inventario para productoId: {}", dto.getProductoId());

        if (inventarioRepository.existsByProductoId(dto.getProductoId())) {
            log.warn("Intento de crear inventario duplicado para productoId: {}", dto.getProductoId());
            throw new DuplicateResourceException("Ya existe un inventario para ese producto");
        }

        ProductoResumenExternoDto producto = obtenerProductoActivoOrThrow(dto.getProductoId());

        Inventario inventario = new Inventario();
        inventario.setProductoId(dto.getProductoId());
        inventario.setStock(dto.getStock());

        inventario = inventarioRepository.save(inventario);

        log.info("Inventario creado correctamente con id: {}", inventario.getId());
        return mapToResponse(inventario, producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponseDto> listar() {
        log.debug("Listando todos los inventarios");

        return inventarioRepository.findAll()
                .stream()
                .map(inventario -> mapToResponse(inventario, null)) // NO enriquecer listado
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponseDto obtenerPorId(Long id) {
        log.debug("Buscando inventario con id: {}", id);

        Inventario inventario = getInventarioOrThrow(id);
        ProductoResumenExternoDto producto = obtenerProductoOrThrow(inventario.getProductoId());

        return mapToResponse(inventario, producto);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponseDto obtenerPorProductoId(Long productoId) {
        log.debug("Buscando inventario por productoId: {}", productoId);

        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> {
                    log.error("Inventario no encontrado para productoId: {}", productoId);
                    return new ResourceNotFoundException("Inventario no encontrado para productoId: " + productoId);
                });

        ProductoResumenExternoDto producto = obtenerProductoOrThrow(productoId);

        return mapToResponse(inventario, producto);
    }

    @Override
    public InventarioResponseDto actualizar(Long id, InventarioUpdateDto dto) {
        log.info("Actualizando inventario con id: {}", id);

        Inventario inventario = getInventarioOrThrow(id);

        if (inventarioRepository.existsByProductoIdAndIdNot(dto.getProductoId(), id)) {
            log.warn("Intento de actualizar inventario {} con productoId duplicado: {}", id, dto.getProductoId());
            throw new DuplicateResourceException("Ya existe otro inventario para ese producto");
        }

        ProductoResumenExternoDto producto = obtenerProductoActivoOrThrow(dto.getProductoId());

        inventario.setProductoId(dto.getProductoId());
        inventario.setStock(dto.getStock());

        inventario = inventarioRepository.save(inventario);

        log.info("Inventario actualizado correctamente con id: {}", id);
        return mapToResponse(inventario, producto);
    }

    @Override
    public MessageResponseDto eliminar(Long id) {
        log.warn("Eliminando inventario con id: {}", id);

        Inventario inventario = getInventarioOrThrow(id);
        inventarioRepository.delete(inventario);

        log.info("Inventario eliminado correctamente con id: {}", id);
        return new MessageResponseDto("Inventario eliminado correctamente");
    }

    private Inventario getInventarioOrThrow(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Inventario no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Inventario no encontrado con id: " + id);
                });
    }

    private ProductoResumenExternoDto obtenerProductoOrThrow(Long productoId) {
        try {
            return productoClient.obtenerResumenPorId(productoId);

        } catch (FeignException.NotFound ex) {
            log.warn("Producto no encontrado en ms_producto. productoId={}", productoId);
            throw new BusinessException("El producto con id " + productoId + " no existe");

        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.error("Error de autenticación/autorización consultando ms_producto");
            throw new BusinessException("No fue posible autenticarse contra ms_producto");

        } catch (FeignException ex) {
            log.error("Error consultando ms_producto. status={}, body={}", ex.status(), ex.contentUTF8());
            throw new BusinessException("Error al consultar ms_producto");
        }
    }

    private ProductoResumenExternoDto obtenerProductoActivoOrThrow(Long productoId) {
        ProductoResumenExternoDto producto = obtenerProductoOrThrow(productoId);

        if (producto.getActivo() == null || !producto.getActivo()) {
            log.warn("Producto inactivo en ms_producto. productoId={}", productoId);
            throw new BusinessException("El producto con id " + productoId + " está inactivo");
        }

        return producto;
    }

    private InventarioResponseDto mapToResponse(Inventario inventario, ProductoResumenExternoDto producto) {
        return new InventarioResponseDto(
                inventario.getId(),
                inventario.getProductoId(),
                inventario.getStock(),
                inventario.getVersion(),
                producto
        );
    }
}