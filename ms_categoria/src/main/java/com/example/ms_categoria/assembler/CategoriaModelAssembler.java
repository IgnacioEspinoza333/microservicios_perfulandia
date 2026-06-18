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
                linkTo(methodOn(CategoriaControllerV2.class).obtener(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).listar()).withRel("categorias")
        );
    }
}