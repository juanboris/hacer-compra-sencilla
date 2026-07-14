package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.ProductoRecetaId;

import java.io.Serial;
import java.io.Serializable;

public class RecetaProductoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ProductoRecetaId id;
    private ProductoUltimoPrecioDTO producto;
    private String cantidad;

    public RecetaProductoDTO() {
    }

    public RecetaProductoDTO(ProductoRecetaId id, ProductoUltimoPrecioDTO producto, String cantidad) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public ProductoRecetaId getId() {
        return id;
    }

    public void setId(ProductoRecetaId id) {
        this.id = id;
    }

    public ProductoUltimoPrecioDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoUltimoPrecioDTO producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
