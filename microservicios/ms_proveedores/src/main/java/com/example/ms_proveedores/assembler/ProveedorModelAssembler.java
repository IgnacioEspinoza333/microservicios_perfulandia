package com.example.ms_proveedores.assembler;

import com.example.ms_proveedores.controller.AbastecimientoControllerV2;
import com.example.ms_proveedores.controller.ProveedorControllerV2;
import com.example.ms_proveedores.dto.ProveedorResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProveedorModelAssembler implements RepresentationModelAssembler<ProveedorResponseDto, EntityModel<ProveedorResponseDto>> {

    @Override
    public EntityModel<ProveedorResponseDto> toModel(ProveedorResponseDto proveedor) {
        return EntityModel.of(
                proveedor,
                linkTo(methodOn(ProveedorControllerV2.class).obtenerPorId(proveedor.getId())).withSelfRel(),
                linkTo(methodOn(ProveedorControllerV2.class).listar()).withRel("proveedores"),
                linkTo(methodOn(AbastecimientoControllerV2.class).listarPorProveedor(proveedor.getId())).withRel("abastecimientos"),
                linkTo(methodOn(ProveedorControllerV2.class).actualizar(proveedor.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(ProveedorControllerV2.class).eliminar(proveedor.getId())).withRel("eliminar")
        );
    }
}
