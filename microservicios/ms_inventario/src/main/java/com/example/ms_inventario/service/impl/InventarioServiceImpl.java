package com.example.ms_inventario.service.impl;

import com.example.ms_inventario.dto.InventarioRequestDto;
import com.example.ms_inventario.dto.InventarioResponseDto;
import com.example.ms_inventario.dto.InventarioUpdateDto;
import com.example.ms_inventario.dto.MessageResponseDto;
import com.example.ms_inventario.exception.DuplicateResourceException;
import com.example.ms_inventario.exception.ResourceNotFoundException;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;
import com.example.ms_inventario.service.InventarioService;
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

    @Override
    public InventarioResponseDto crear(InventarioRequestDto dto) {
        log.info("Iniciando creación de inventario para productoId: {}", dto.getProductoId());

        if (inventarioRepository.existsByProductoId(dto.getProductoId())) {
            log.warn("Intento de crear inventario duplicado para productoId: {}", dto.getProductoId());
            throw new DuplicateResourceException("Ya existe un inventario para ese producto");
        }

        Inventario inventario = new Inventario();
        inventario.setProductoId(dto.getProductoId());
        inventario.setStock(dto.getStock());

        inventario = inventarioRepository.save(inventario);

        log.info("Inventario creado correctamente con id: {}", inventario.getId());
        return mapToResponse(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponseDto> listar() {
        log.debug("Listando todos los inventarios");
        return inventarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponseDto obtenerPorId(Long id) {
        log.debug("Buscando inventario con id: {}", id);
        Inventario inventario = getInventarioOrThrow(id);
        return mapToResponse(inventario);
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

        return mapToResponse(inventario);
    }

    @Override
    public InventarioResponseDto actualizar(Long id, InventarioUpdateDto dto) {
        log.info("Actualizando inventario con id: {}", id);

        Inventario inventario = getInventarioOrThrow(id);

        if (inventarioRepository.existsByProductoIdAndIdNot(dto.getProductoId(), id)) {
            log.warn("Intento de actualizar inventario {} con productoId duplicado: {}", id, dto.getProductoId());
            throw new DuplicateResourceException("Ya existe otro inventario para ese producto");
        }

        inventario.setProductoId(dto.getProductoId());
        inventario.setStock(dto.getStock());

        inventario = inventarioRepository.save(inventario);

        log.info("Inventario actualizado correctamente con id: {}", id);
        return mapToResponse(inventario);
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

    private InventarioResponseDto mapToResponse(Inventario inventario) {
        return new InventarioResponseDto(
                inventario.getId(),
                inventario.getProductoId(),
                inventario.getStock(),
                inventario.getVersion()
        );
    }
}