package com.example.ms_boleta.controller;

import com.example.ms_boleta.assembler.BoletaModelAssembler;
import com.example.ms_boleta.dto.BoletaRequestDTO;
import com.example.ms_boleta.dto.BoletaResponseDTO;
import com.example.ms_boleta.service.BoletaService;
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
@RequestMapping("/api/v2/boletas")
@RequiredArgsConstructor
@Tag(name = "Boletas", description = "API para la gestión de boletas con soporte HATEOAS")
public class BoletaControllerV2 {

    private final BoletaService boletaService;
    private final BoletaModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear boleta", description = "Registra una nueva boleta en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Boleta creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoletaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<BoletaResponseDTO>> crear(@RequestBody BoletaRequestDTO request) {
        BoletaResponseDTO nuevaBoleta = boletaService.crearBoleta(request);

        return ResponseEntity
                .created(linkTo(methodOn(BoletaControllerV2.class).obtener(nuevaBoleta.getId())).toUri())
                .body(assembler.toModel(nuevaBoleta));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener boleta por ID", description = "Obtiene una boleta según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Boleta encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoletaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    public EntityModel<BoletaResponseDTO> obtener(@PathVariable Long id) {
        BoletaResponseDTO boleta = boletaService.obtenerBoleta(id);
        return assembler.toModel(boleta);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar boletas", description = "Obtiene todas las boletas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<BoletaResponseDTO>> listar() {
        List<EntityModel<BoletaResponseDTO>> boletas = boletaService.listarBoletas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                boletas,
                linkTo(methodOn(BoletaControllerV2.class).listar()).withSelfRel()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar boleta", description = "Elimina una boleta por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Boleta eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    public ResponseEntity<Void> eliminarBoleta(@PathVariable Long id) {
        boletaService.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar boleta", description = "Actualiza una boleta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Boleta actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoletaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    public ResponseEntity<EntityModel<BoletaResponseDTO>> actualizarBoleta(
            @PathVariable Long id,
            @Valid @RequestBody BoletaRequestDTO request) {

        BoletaResponseDTO actualizada = boletaService.actualizarBoleta(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }
}