package com.example.ms_envio.controller;

import com.example.ms_envio.assembler.EnvioModelAssembler;
import com.example.ms_envio.dto.EnvioRequestDTO;
import com.example.ms_envio.dto.EnvioResponseDTO;
import com.example.ms_envio.service.EnvioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/envios")
@RequiredArgsConstructor
public class EnvioControllerV2 {

    private final EnvioService envioService;
    private final EnvioModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EnvioResponseDTO>> crear(@RequestBody EnvioRequestDTO request) {
        EnvioResponseDTO nuevoEnvio = envioService.crearEnvio(request);

        return ResponseEntity
                .created(linkTo(methodOn(EnvioControllerV2.class).obtener(nuevoEnvio.getId())).toUri())
                .body(assembler.toModel(nuevoEnvio));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<EnvioResponseDTO> obtener(@PathVariable Long id) {
        EnvioResponseDTO envio = envioService.obtenerEnvio(id);
        return assembler.toModel(envio);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<EnvioResponseDTO>> listar() {
        List<EntityModel<EnvioResponseDTO>> envios = envioService.listarEnvios().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                envios,
                linkTo(methodOn(EnvioControllerV2.class).listar()).withSelfRel()
        );
    }

    @PutMapping(value = "/{id}/cancelar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        envioService.cancelarEnvio(id);
        return ResponseEntity.noContent().build();
    }

     @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EnvioResponseDTO>> actualizarEnvio(
            @PathVariable Long id,
            @Valid @RequestBody EnvioRequestDTO request) {

        EnvioResponseDTO actualizado = envioService.actualizarEnvio(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
}
}
