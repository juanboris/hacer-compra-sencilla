package com.juanboris.springboot.backend.listaCompra.models.DTO;

public interface ProductoProjectionDTO {
    public Long getId();
    public String getNombre();
    public String getPrecio();
    public String getMarca();
    public TipoProductoProjectionDTO getTipo();
    public String getMediaPrecio();
}
