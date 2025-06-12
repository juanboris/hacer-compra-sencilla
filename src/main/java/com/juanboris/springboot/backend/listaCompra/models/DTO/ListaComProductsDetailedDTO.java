package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCompProductoId;

import java.io.Serial;
import java.io.Serializable;


public class ListaComProductsDetailedDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ListaCompProductoId id;
    private ProductoUltimoPrecioDTO producto;
    private String cantidad;
    private Boolean comprado;

    public ListaComProductsDetailedDTO(ListaCompProductoId id, ProductoUltimoPrecioDTO producto, String cantidad, Boolean comprado) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.comprado = comprado;
    }

    public ListaComProductsDetailedDTO() {
    }

    public ListaCompProductoId getId() {
        return id;
    }

    public void setId(ListaCompProductoId id) {
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

    public Boolean getComprado() {
        return comprado;
    }

    public void setComprado(Boolean comprado) {
        this.comprado = comprado;
    }
}
