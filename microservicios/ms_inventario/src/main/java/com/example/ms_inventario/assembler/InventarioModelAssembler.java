package com.example.ms_inventario.assembler;

import com.example.ms_inventario.controller.InventarioControllerV2;
import com.example.ms_inventario.dto.InventarioResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<InventarioResponseDto, EntityModel<InventarioResponseDto>> {

    @Override
    public EntityModel<InventarioResponseDto> toModel(InventarioResponseDto inventario) {
        return EntityModel.of(
                inventario,
                linkTo(methodOn(InventarioControllerV2.class).obtenerPorId(inventario.getId())).withSelfRel(),
                linkTo(methodOn(InventarioControllerV2.class).listar()).withRel("inventarios"),
                linkTo(methodOn(InventarioControllerV2.class).actualizarInventario(inventario.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(InventarioControllerV2.class).eliminarInventario(inventario.getId())).withRel("eliminar")
        );
    }
}