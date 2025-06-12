package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCompProductoId;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;

public interface ListaComProductsDetailedProjectionDTO {
    public ListaCompProductoId getId();
    public ProductoProjectionDTO getProducto();
    public String getCantidad();
    public Boolean getComprado();
}