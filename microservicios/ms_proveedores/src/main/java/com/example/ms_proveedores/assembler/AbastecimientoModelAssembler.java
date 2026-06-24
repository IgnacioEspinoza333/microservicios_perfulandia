package com.example.ms_proveedores.assembler;

import com.example.ms_proveedores.controller.AbastecimientoControllerV2;
import com.example.ms_proveedores.dto.AbastecimientoResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AbastecimientoModelAssembler implements RepresentationModelAssembler<AbastecimientoResponseDto, EntityModel<AbastecimientoResponseDto>> {

    @Override
    public EntityModel<AbastecimientoResponseDto> toModel(AbastecimientoResponseDto abastecimiento) {
        return EntityModel.of(
                abastecimiento,
                linkTo(methodOn(AbastecimientoControllerV2.class).obtenerPorId(abastecimiento.getId())).withSelfRel(),
                linkTo(methodOn(AbastecimientoControllerV2.class).listar()).withRel("abastecimientos"),
                linkTo(methodOn(AbastecimientoControllerV2.class).actualizarAbastecimiento(abastecimiento.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(AbastecimientoControllerV2.class).eliminarAbastecimiento(abastecimiento.getId())).withRel("eliminar")
        );
    }
}