package com.example.ms_clientes.controller;

import com.example.ms_clientes.dto.ClienteRequestDto;
import com.example.ms_clientes.dto.ClienteResponseDto;
import com.example.ms_clientes.dto.ClienteUpdateDto;
import com.example.ms_clientes.dto.MessageResponseDto;
import com.example.ms_clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDto crear(@Valid @RequestBody ClienteRequestDto dto) {
        log.info("Solicitud para crear cliente con email: {}", dto.getEmail());
        return clienteService.crear(dto);
    }

    @GetMapping
    public List<ClienteResponseDto> listar() {
        log.debug("Solicitud para listar clientes");
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponseDto obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud para obtener cliente con id: {}", id);
        return clienteService.obtenerPorId(id);
    }
   //listo 
    @PutMapping("/{id}")
    public ClienteResponseDto actualizar(@PathVariable Long id,
                                         @Valid @RequestBody ClienteUpdateDto dto) {
        log.info("Solicitud para actualizar cliente con id: {}", id);
        return clienteService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto eliminar(@PathVariable Long id) {
        log.warn("Solicitud para eliminar cliente con id: {}", id);
        return clienteService.eliminar(id);
    }
}