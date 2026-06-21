package com.example.ms_usuario.assembler;

import com.example.ms_usuario.controller.UsuarioControllerV2;
import com.example.ms_usuario.dto.UsuarioResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioResponseDto, EntityModel<UsuarioResponseDto>> {

    @Override
    public EntityModel<UsuarioResponseDto> toModel(UsuarioResponseDto usuario) {
        return EntityModel.of(
                usuario,
                linkTo(methodOn(UsuarioControllerV2.class).obtenerPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class).listar()).withRel("usuarios"),
                linkTo(methodOn(UsuarioControllerV2.class).listarRoles(usuario.getId())).withRel("roles")
        );
    }
}