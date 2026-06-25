package com.example.ms_producto.client;

import com.example.ms_producto.config.FeignBasicAuthConfig;
import com.example.ms_producto.dto.ProveedorExternoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-proveedores",
        configuration = FeignBasicAuthConfig.class
)
public interface ProveedorClient {

    @GetMapping(value = "/api/v2/proveedores/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    ProveedorExternoDto obtenerPorId(@PathVariable("id") Long id);
}