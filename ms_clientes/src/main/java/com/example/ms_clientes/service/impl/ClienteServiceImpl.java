package com.example.ms_clientes.service.impl;

import com.example.ms_clientes.dto.ClienteRequestDto;
import com.example.ms_clientes.dto.ClienteResponseDto;
import com.example.ms_clientes.dto.ClienteUpdateDto;
import com.example.ms_clientes.dto.MessageResponseDto;
import com.example.ms_clientes.exception.DuplicateResourceException;
import com.example.ms_clientes.exception.ResourceNotFoundException;
import com.example.ms_clientes.model.Cliente;
import com.example.ms_clientes.repository.ClienteRepository;
import com.example.ms_clientes.repository.DireccionRepository;
import com.example.ms_clientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;

    @Override
    @Transactional
    public ClienteResponseDto crear(ClienteRequestDto dto) {
        log.info("Iniciando creación de cliente con email: {}", dto.getEmail());

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            log.warn("Intento de crear cliente con email duplicado: {}", dto.getEmail());
            throw new DuplicateResourceException("Ya existe un cliente con ese email");
        }

        Cliente cliente = new Cliente();
        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setActivo(dto.getActivo());

        cliente = clienteRepository.save(cliente);

        log.info("Cliente creado correctamente con id: {}", cliente.getId());
        return mapToResponse(cliente);
    }

    @Override
    public List<ClienteResponseDto> listar() {
        log.debug("Listando todos los clientes");
        return clienteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ClienteResponseDto obtenerPorId(Long id) {
        log.debug("Buscando cliente con id: {}", id);
        Cliente cliente = getClienteOrThrow(id);
        return mapToResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDto actualizar(Long id, ClienteUpdateDto dto) {
        log.info("Actualizando cliente con id: {}", id);

        Cliente cliente = getClienteOrThrow(id);

        if (clienteRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            log.warn("Intento de actualizar cliente {} con email duplicado: {}", id, dto.getEmail());
            throw new DuplicateResourceException("Ya existe otro cliente con ese email");
        }

        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setActivo(dto.getActivo());

        cliente = clienteRepository.save(cliente);

        log.info("Cliente actualizado correctamente con id: {}", id);
        return mapToResponse(cliente);
    }

    @Override
    @Transactional
    public MessageResponseDto eliminar(Long id) {
        log.warn("Eliminando cliente con id: {}", id);

        Cliente cliente = getClienteOrThrow(id);

        if (direccionRepository.existsByClienteId(id)) {
            log.debug("Eliminando direcciones asociadas al cliente con id: {}", id);
            direccionRepository.deleteByClienteId(id);
        }

        clienteRepository.delete(cliente);

        log.info("Cliente eliminado correctamente con id: {}", id);
        return new MessageResponseDto("Cliente eliminado correctamente");
    }

    private Cliente getClienteOrThrow(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con id: " + id);
                });
    }

    private ClienteResponseDto mapToResponse(Cliente cliente) {
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getActivo(),
                cliente.getVersion()
        );
    }
}