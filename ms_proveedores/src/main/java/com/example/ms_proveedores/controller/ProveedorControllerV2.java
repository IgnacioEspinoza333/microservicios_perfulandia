package com.example.ms_proveedores.controller;

import com.example.ms_proveedores.assembler.ProveedorModelAssembler;
import com.example.ms_proveedores.dto.ProveedorRequestDto;
import com.example.ms_proveedores.dto.ProveedorResponseDto;
import com.example.ms_proveedores.dto.ProveedorUpdateDto;
import com.example.ms_proveedores.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/proveedores")
@RequiredArgsConstructor
@Slf4j
public class ProveedorControllerV2 {

    private final ProveedorService proveedorService;
    private final ProveedorModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProveedorResponseDto>> crear(@Valid @RequestBody ProveedorRequestDto dto) {
        log.info("Solicitud v2 para crear proveedor con email: {}", dto.getEmail());

        ProveedorResponseDto nuevoProveedor = proveedorService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(ProveedorControllerV2.class).obtenerPorId(nuevoProveedor.getId())).toUri())
                .body(assembler.toModel(nuevoProveedor));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ProveedorResponseDto>> listar() {
        log.debug("Solicitud v2 para listar proveedores");

        List<EntityModel<ProveedorResponseDto>> proveedores = proveedorService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                proveedores,
                linkTo(methodOn(ProveedorControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ProveedorResponseDto> obtenerPorId(@PathVariable Long id) {
        log.debug("Solicitud v2 para obtener proveedor con id: {}", id);

        ProveedorResponseDto proveedor = proveedorService.obtenerPorId(id);
        return assembler.toModel(proveedor);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProveedorResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorUpdateDto dto) {

        log.info("Solicitud v2 para actualizar proveedor con id: {}", id);

        ProveedorResponseDto actualizado = proveedorService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Solicitud v2 para eliminar proveedor con id: {}", id);

        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}