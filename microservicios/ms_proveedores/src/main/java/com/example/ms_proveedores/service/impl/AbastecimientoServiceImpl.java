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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AbastecimientoServiceImpl implements AbastecimientoService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "COMPLETADO", "CANCELADO");

    private final AbastecimientoRepository abastecimientoRepository;
    private final ProveedorRepository proveedorRepository;

    @Override
    public AbastecimientoResponseDto crear(AbastecimientoRequestDto dto) {
        log.info("Creando abastecimiento para proveedorId: {} y productoId: {}", dto.getProveedorId(), dto.getProductoId());

        Proveedor proveedor = getProveedorOrThrow(dto.getProveedorId());

        String estado = (dto.getEstado() == null || dto.getEstado().isBlank())
                ? "PENDIENTE"
                : dto.getEstado().trim().toUpperCase();

        validarEstado(estado);

        Abastecimiento abastecimiento = new Abastecimiento();
        abastecimiento.setProveedorId(dto.getProveedorId());
        abastecimiento.setProductoId(dto.getProductoId());
        abastecimiento.setCantidad(dto.getCantidad());
        abastecimiento.setEstado(estado);
        abastecimiento.setFechaCreacion(Instant.now());

        abastecimiento = abastecimientoRepository.save(abastecimiento);

        log.info("Abastecimiento creado correctamente con id: {}", abastecimiento.getId());
        return mapToResponse(abastecimiento, proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbastecimientoResponseDto> listar() {
        log.debug("Listando todos los abastecimientos");
        return abastecimientoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbastecimientoResponseDto> listarPorProveedor(Long proveedorId) {
        log.debug("Listando abastecimientos del proveedor con id: {}", proveedorId);

        getProveedorOrThrow(proveedorId);

        return abastecimientoRepository.findByProveedorId(proveedorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbastecimientoResponseDto> listarPorProducto(Long productoId) {
        log.debug("Listando abastecimientos del producto con id: {}", productoId);

        return abastecimientoRepository.findByProductoId(productoId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AbastecimientoResponseDto obtenerPorId(Long id) {
        log.debug("Buscando abastecimiento con id: {}", id);
        Abastecimiento abastecimiento = getAbastecimientoOrThrow(id);
        return mapToResponse(abastecimiento);
    }

    @Override
    public AbastecimientoResponseDto actualizar(Long id, AbastecimientoUpdateDto dto) {
        log.info("Actualizando abastecimiento con id: {}", id);

        Abastecimiento abastecimiento = getAbastecimientoOrThrow(id);
        Proveedor proveedor = getProveedorOrThrow(dto.getProveedorId());
        

        String estado = dto.getEstado().trim().toUpperCase();
        validarEstado(estado);

        // actualizar proveedor si viene en el DTO
        if (dto.getNombreProveedor() != null && !dto.getNombreProveedor().isBlank()) {
        proveedor.setNombre(dto.getNombreProveedor());
        proveedorRepository.save(proveedor);
    }

        abastecimiento.setProveedorId(dto.getProveedorId());
        abastecimiento.setProductoId(dto.getProductoId());
        abastecimiento.setCantidad(dto.getCantidad());
        abastecimiento.setEstado(estado);

        abastecimiento = abastecimientoRepository.save(abastecimiento);

        log.info("Abastecimiento actualizado correctamente con id: {}", id);
        return mapToResponse(abastecimiento, proveedor);
    }

    @Override
    public MessageResponseDto eliminar(Long id) {
        log.warn("Eliminando abastecimiento con id: {}", id);

        Abastecimiento abastecimiento = getAbastecimientoOrThrow(id);
        abastecimientoRepository.delete(abastecimiento);

        log.info("Abastecimiento eliminado correctamente con id: {}", id);
        return new MessageResponseDto("Abastecimiento eliminado correctamente");
    }

    private void validarEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado)) {
            log.warn("Estado inválido en abastecimiento: {}", estado);
            throw new BusinessException("El estado debe ser PENDIENTE, COMPLETADO o CANCELADO");
        }
    }

    private Proveedor getProveedorOrThrow(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Proveedor no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Proveedor no encontrado con id: " + id);
                });
    }

    private Abastecimiento getAbastecimientoOrThrow(Long id) {
        return abastecimientoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Abastecimiento no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Abastecimiento no encontrado con id: " + id);
                });
    }

    private AbastecimientoResponseDto mapToResponse(Abastecimiento abastecimiento) {
        Proveedor proveedor = proveedorRepository.findById(abastecimiento.getProveedorId()).orElse(null);

        String nombreProveedor = proveedor != null ? proveedor.getNombre() : null;

        return new AbastecimientoResponseDto(
                abastecimiento.getId(),
                abastecimiento.getProveedorId(),
                nombreProveedor,
                abastecimiento.getProductoId(),
                abastecimiento.getCantidad(),
                abastecimiento.getEstado(),
                abastecimiento.getFechaCreacion(),
                abastecimiento.getVersion()
        );
    }

    private AbastecimientoResponseDto mapToResponse(Abastecimiento abastecimiento, Proveedor proveedor) {
        return new AbastecimientoResponseDto(
                abastecimiento.getId(),
                abastecimiento.getProveedorId(),
                proveedor.getNombre(),
                abastecimiento.getProductoId(),
                abastecimiento.getCantidad(),
                abastecimiento.getEstado(),
                abastecimiento.getFechaCreacion(),
                abastecimiento.getVersion()
        );
    }
}