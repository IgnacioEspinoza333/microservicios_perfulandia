package com.example.ms_usuario.assembler;

import com.example.ms_usuario.controller.PermisoControllerV2;
import com.example.ms_usuario.dto.PermisoResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PermisoModelAssembler implements RepresentationModelAssembler<PermisoResponseDto, EntityModel<PermisoResponseDto>> {

    @Override
    public EntityModel<PermisoResponseDto> toModel(PermisoResponseDto permiso) {
        return EntityModel.of(
                permiso,
                linkTo(methodOn(PermisoControllerV2.class).obtenerPorId(permiso.getId())).withSelfRel(),
                linkTo(methodOn(PermisoControllerV2.class).listar()).withRel("permisos"),
                linkTo(methodOn(PermisoControllerV2.class).actualizarPermiso(permiso.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(PermisoControllerV2.class).eliminarPermiso(permiso.getId())).withRel("eliminar")
                
        );
    }
}
