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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final AbastecimientoRepository abastecimientoRepository;

    @Override
    public ProveedorResponseDto crear(ProveedorRequestDto dto) {
        log.info("Iniciando creación de proveedor con email: {}", dto.getEmail());

        if (dto.getEmail() != null && !dto.getEmail().isBlank() && proveedorRepository.existsByEmail(dto.getEmail())) {
            log.warn("Intento de crear proveedor con email duplicado: {}", dto.getEmail());
            throw new DuplicateResourceException("Ya existe un proveedor con ese email");
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setActivo(dto.getActivo());

        proveedor = proveedorRepository.save(proveedor);

        log.info("Proveedor creado correctamente con id: {}", proveedor.getId());
        return mapToResponse(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorResponseDto> listar() {
        log.debug("Listando todos los proveedores");
        return proveedorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDto obtenerPorId(Long id) {
        log.debug("Buscando proveedor con id: {}", id);
        Proveedor proveedor = getProveedorOrThrow(id);
        return mapToResponse(proveedor);
    }

    @Override
    public ProveedorResponseDto actualizar(Long id, ProveedorUpdateDto dto) {
        log.info("Actualizando proveedor con id: {}", id);

        Proveedor proveedor = getProveedorOrThrow(id);

        if (dto.getEmail() != null && !dto.getEmail().isBlank()
                && proveedorRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            log.warn("Intento de actualizar proveedor {} con email duplicado: {}", id, dto.getEmail());
            throw new DuplicateResourceException("Ya existe otro proveedor con ese email");
        }

        proveedor.setNombre(dto.getNombre());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setActivo(dto.getActivo());

        proveedor = proveedorRepository.save(proveedor);

        log.info("Proveedor actualizado correctamente con id: {}", id);
        return mapToResponse(proveedor);
    }

    @Override
    public MessageResponseDto eliminar(Long id) {
        log.warn("Eliminando proveedor con id: {}", id);

        Proveedor proveedor = getProveedorOrThrow(id);

        if (abastecimientoRepository.existsByProveedorId(id)) {
            log.debug("Eliminando abastecimientos asociados al proveedor con id: {}", id);
            abastecimientoRepository.deleteByProveedorId(id);
        }

        proveedorRepository.delete(proveedor);

        log.info("Proveedor eliminado correctamente con id: {}", id);
        return new MessageResponseDto("Proveedor eliminado correctamente");
    }

    private Proveedor getProveedorOrThrow(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Proveedor no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Proveedor no encontrado con id: " + id);
                });
    }

    private ProveedorResponseDto mapToResponse(Proveedor proveedor) {
        return new ProveedorResponseDto(
                proveedor.getId(),
                proveedor.getNombre(),
                proveedor.getEmail(),
                proveedor.getTelefono(),
                proveedor.getDireccion(),
                proveedor.getActivo(),
                proveedor.getVersion()
        );
    }
}