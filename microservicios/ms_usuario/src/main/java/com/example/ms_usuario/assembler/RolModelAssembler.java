package com.example.ms_usuario.assembler;

import com.example.ms_usuario.controller.RolControllerV2;
import com.example.ms_usuario.dto.RolResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<RolResponseDto, EntityModel<RolResponseDto>> {

    @Override
    public EntityModel<RolResponseDto> toModel(RolResponseDto rol) {
        return EntityModel.of(
                rol,
                linkTo(methodOn(RolControllerV2.class).obtenerPorId(rol.getId())).withSelfRel(),
                linkTo(methodOn(RolControllerV2.class).listar()).withRel("roles"),
                 linkTo(methodOn(RolControllerV2.class).actualizar(rol.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(RolControllerV2.class).eliminar(rol.getId())).withRel("eliminar")
        );
    }
}