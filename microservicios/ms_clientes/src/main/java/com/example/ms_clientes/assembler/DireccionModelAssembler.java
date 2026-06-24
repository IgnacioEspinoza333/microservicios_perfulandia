package com.example.ms_clientes.assembler;

import com.example.ms_clientes.controller.DireccionControllerV2;
import com.example.ms_clientes.dto.DireccionResponseDto;
import com.example.ms_clientes.dto.DireccionUpdateDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DireccionModelAssembler implements RepresentationModelAssembler<DireccionResponseDto, EntityModel<DireccionResponseDto>> {

    @Override
    public EntityModel<DireccionResponseDto> toModel(DireccionResponseDto direccion) {
        return EntityModel.of(
                direccion,
                linkTo(methodOn(DireccionControllerV2.class).obtenerDireccionPorId(direccion.getId())).withSelfRel(),
                linkTo(methodOn(DireccionControllerV2.class).listarDirecciones()).withRel("direcciones"),
                linkTo(methodOn(DireccionControllerV2.class).actualizarDireccion(direccion.getId(), new DireccionUpdateDto())).withRel("actualizar"),
                linkTo(methodOn(DireccionControllerV2.class).eliminarDireccion(direccion.getId())).withRel("eliminar")
        );
    }
}
