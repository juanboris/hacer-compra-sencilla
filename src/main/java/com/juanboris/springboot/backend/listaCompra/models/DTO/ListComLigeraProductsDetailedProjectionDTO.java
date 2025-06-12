package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public interface ListComLigeraProductsDetailedProjectionDTO {
    public Long getId();
    public BigDecimal getPrecio();
    public Date getCreated();
    public Set<ListaComProductsDetailedProjectionDTO> getProductos();
    public Usuario getUsuario();
}
