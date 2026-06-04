package com.example.ms_clientes.service.impl;

import com.example.ms_clientes.dto.DireccionRequestDto;
import com.example.ms_clientes.dto.DireccionResponseDto;
import com.example.ms_clientes.dto.DireccionUpdateDto;
import com.example.ms_clientes.dto.MessageResponseDto;
import com.example.ms_clientes.exception.ResourceNotFoundException;
import com.example.ms_clientes.model.Cliente;
import com.example.ms_clientes.model.Direccion;
import com.example.ms_clientes.repository.ClienteRepository;
import com.example.ms_clientes.repository.DireccionRepository;
import com.example.ms_clientes.service.DireccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public DireccionResponseDto crear(Long clienteId, DireccionRequestDto dto) {
        log.info("Creando dirección para cliente con id: {}", clienteId);

        Cliente cliente = getClienteOrThrow(clienteId);

        if (Boolean.TRUE.equals(dto.getPrincipal())) {
            desmarcarPrincipalActual(clienteId);
        }

        Direccion direccion = new Direccion();
        direccion.setCliente(cliente);
        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setDepto(dto.getDepto());
        direccion.setComuna(dto.getComuna());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setReferencia(dto.getReferencia());
        direccion.setPrincipal(dto.getPrincipal());

        direccion = direccionRepository.save(direccion);

        log.info("Dirección creada correctamente con id: {}", direccion.getId());
        return mapToResponse(direccion);
    }

    @Override
    public List<DireccionResponseDto> listar() {
        log.debug("Listando todas las direcciones");
        return direccionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<DireccionResponseDto> listarPorCliente(Long clienteId) {
        log.debug("Listando direcciones del cliente con id: {}", clienteId);

        getClienteOrThrow(clienteId);

        return direccionRepository.findByClienteId(clienteId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DireccionResponseDto obtenerPorId(Long id) {
        log.debug("Buscando dirección con id: {}", id);
        Direccion direccion = getDireccionOrThrow(id);
        return mapToResponse(direccion);
    }

    @Override
    @Transactional
    public DireccionResponseDto actualizar(Long id, DireccionUpdateDto dto) {
        log.info("Actualizando dirección con id: {}", id);

        Direccion direccion = getDireccionOrThrow(id);

        if (Boolean.TRUE.equals(dto.getPrincipal())) {
            desmarcarPrincipalActual(direccion.getCliente().getId());

            // si la actual será la principal, la dejamos true después
        }

        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setDepto(dto.getDepto());
        direccion.setComuna(dto.getComuna());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setReferencia(dto.getReferencia());
        direccion.setPrincipal(dto.getPrincipal());

        direccion = direccionRepository.save(direccion);

        log.info("Dirección actualizada correctamente con id: {}", id);
        return mapToResponse(direccion);
    }

    @Override
    @Transactional
    public MessageResponseDto eliminar(Long id) {
        log.warn("Eliminando dirección con id: {}", id);

        Direccion direccion = getDireccionOrThrow(id);
        direccionRepository.delete(direccion);

        log.info("Dirección eliminada correctamente con id: {}", id);
        return new MessageResponseDto("Dirección eliminada correctamente");
    }

    private Cliente getClienteOrThrow(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con id: " + id);
                });
    }

    private Direccion getDireccionOrThrow(Long id) {
        return direccionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Dirección no encontrada con id: {}", id);
                    return new ResourceNotFoundException("Dirección no encontrada con id: " + id);
                });
    }

    private void desmarcarPrincipalActual(Long clienteId) {
        direccionRepository.findByClienteIdAndPrincipalTrue(clienteId).ifPresent(actualPrincipal -> {
            log.debug("Desmarcando dirección principal actual con id: {}", actualPrincipal.getId());
            actualPrincipal.setPrincipal(false);
            direccionRepository.save(actualPrincipal);
        });
    }

    private DireccionResponseDto mapToResponse(Direccion direccion) {
        Cliente cliente = direccion.getCliente();

        return new DireccionResponseDto(
                direccion.getId(),
                cliente.getId(),
                cliente.getNombres() + " " + cliente.getApellidos(),
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getDepto(),
                direccion.getComuna(),
                direccion.getCiudad(),
                direccion.getCodigoPostal(),
                direccion.getReferencia(),
                direccion.getPrincipal(),
                direccion.getVersion()
        );
    }
}