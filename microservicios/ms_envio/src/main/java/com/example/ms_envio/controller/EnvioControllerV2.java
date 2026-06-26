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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/envios")
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "API para la gestión de envíos con soporte HATEOAS")
public class EnvioControllerV2 {

    private final EnvioService envioService;
    private final EnvioModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear envío", description = "Registra un nuevo envío en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Envío creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnvioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<EnvioResponseDTO>> crear(@RequestBody EnvioRequestDTO request) {
        EnvioResponseDTO nuevoEnvio = envioService.crearEnvio(request);

        return ResponseEntity
                .created(linkTo(methodOn(EnvioControllerV2.class).obtener(nuevoEnvio.getId())).toUri())
                .body(assembler.toModel(nuevoEnvio));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener envío por ID", description = "Obtiene un envío según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envío encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnvioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public EntityModel<EnvioResponseDTO> obtener(@PathVariable Long id) {
        EnvioResponseDTO envio = envioService.obtenerEnvio(id);
        return assembler.toModel(envio);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar envíos", description = "Obtiene todos los envíos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
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
    @Operation(summary = "Cancelar envío", description = "Cancela un envío existente mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Envío cancelado correctamente"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        envioService.cancelarEnvio(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar envío", description = "Actualiza la información de un envío existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envío actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EnvioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public ResponseEntity<EntityModel<EnvioResponseDTO>> actualizarEnvio(
            @PathVariable Long id,
            @Valid @RequestBody EnvioRequestDTO request) {

        EnvioResponseDTO actualizado = envioService.actualizarEnvio(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar envío", description = "Elimina un envío por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Envío eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        envioService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }
}