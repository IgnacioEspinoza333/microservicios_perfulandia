package com.example.ms_usuario.assembler;

import com.example.ms_usuario.controller.EmpleadoControllerV2;
import com.example.ms_usuario.dto.EmpleadoResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmpleadoModelAssembler implements RepresentationModelAssembler<EmpleadoResponseDto, EntityModel<EmpleadoResponseDto>> {

    @Override
    public EntityModel<EmpleadoResponseDto> toModel(EmpleadoResponseDto empleado) {
        return EntityModel.of(
                empleado,
                linkTo(methodOn(EmpleadoControllerV2.class).obtenerPorId(empleado.getId())).withSelfRel(),
                linkTo(methodOn(EmpleadoControllerV2.class).listar()).withRel("empleados"),
                linkTo(methodOn(EmpleadoControllerV2.class).actualizar(empleado.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(EmpleadoControllerV2.class).eliminar(empleado.getId())).withRel("eliminar")
        );
    }
}