package com.example.ms_producto.assembler;

import com.example.ms_producto.controller.ProductoControllerV2;
import com.example.ms_producto.dto.ProductoResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoResponseDto, EntityModel<ProductoResponseDto>> {

    @Override
    public EntityModel<ProductoResponseDto> toModel(ProductoResponseDto producto) {
        return EntityModel.of(
                producto,
                linkTo(methodOn(ProductoControllerV2.class).obtenerPorId(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).listar()).withRel("productos"),
                linkTo(methodOn(ProductoControllerV2.class).actualizarProducto(producto.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(ProductoControllerV2.class).eliminarProducto(producto.getId())).withRel("eliminar")
        );
    }
}