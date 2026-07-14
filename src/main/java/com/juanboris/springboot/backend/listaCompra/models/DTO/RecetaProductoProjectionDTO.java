package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.ProductoRecetaId;

public interface RecetaProductoProjectionDTO {
    public ProductoRecetaId getId();
    public ProductoProjectionDTO getProducto();
    public String getCantidad();
}