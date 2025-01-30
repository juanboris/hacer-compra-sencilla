package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

import java.math.BigDecimal;
import java.util.Date;

public interface ListaComLigeraProjectionDTO {
    public Long getId();
    public Date getCreated();
    public Date getModified();
    public BigDecimal getPrecio();
    public Usuario getUsuario();
}
