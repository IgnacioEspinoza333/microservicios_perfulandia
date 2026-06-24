package com.example.ms_producto.client;

import com.example.ms_producto.config.FeignBasicAuthConfig;
import com.example.ms_producto.dto.CategoriaExternaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms_categoria",
        configuration = FeignBasicAuthConfig.class
)
public interface CategoriaClient {

    @GetMapping(value = "/api/v2/categorias/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    CategoriaExternaDto obtenerPorId(@PathVariable("id") Long id);
}
