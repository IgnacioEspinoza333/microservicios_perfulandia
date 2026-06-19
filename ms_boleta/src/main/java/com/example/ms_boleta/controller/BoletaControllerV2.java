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

@RestController
@RequestMapping("/api/v2/boletas")
@RequiredArgsConstructor
public class BoletaControllerV2 {

    private final BoletaService boletaService;
    private final BoletaModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BoletaResponseDTO>> crear(@RequestBody BoletaRequestDTO request) {
        BoletaResponseDTO nuevaBoleta = boletaService.crearBoleta(request);

        return ResponseEntity
                .created(linkTo(methodOn(BoletaControllerV2.class).obtener(nuevaBoleta.getId())).toUri())
                .body(assembler.toModel(nuevaBoleta));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<BoletaResponseDTO> obtener(@PathVariable Long id) {
        BoletaResponseDTO boleta = boletaService.obtenerBoleta(id);
        return assembler.toModel(boleta);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<BoletaResponseDTO>> listar() {
        List<EntityModel<BoletaResponseDTO>> boletas = boletaService.listarBoletas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                boletas,
                linkTo(methodOn(BoletaControllerV2.class).listar()).withSelfRel()
        );
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boletaService.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }
     @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BoletaResponseDTO>> actualizarBoleta(
            @PathVariable Long id,
            @Valid @RequestBody BoletaRequestDTO request) {

        BoletaResponseDTO actualizada = boletaService.actualizarBoleta(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }
}