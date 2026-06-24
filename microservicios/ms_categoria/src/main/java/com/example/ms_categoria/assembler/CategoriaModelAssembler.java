package com.example.ms_categoria.assembler;

import com.example.ms_categoria.controller.CategoriaControllerV2;
import com.example.ms_categoria.dto.CategoriaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaResponseDTO, EntityModel<CategoriaResponseDTO>> {

    @Override
    public EntityModel<CategoriaResponseDTO> toModel(CategoriaResponseDTO categoria) {
        return EntityModel.of(
                categoria,
                linkTo(methodOn(CategoriaControllerV2.class).obtenerCategoria(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).listarCategorias()).withRel("categorias"),
                linkTo(methodOn(CategoriaControllerV2.class).actualizarCategoria(categoria.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(CategoriaControllerV2.class).eliminarCategoria(categoria.getId())).withRel("eliminar")
        );
    }
}