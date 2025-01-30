package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

import java.math.BigDecimal;
import java.util.Date;

public class ListaComLigeraDTO {
    private Long id;
    private Date created;
    private Date modified;
    private BigDecimal precio;
    private Usuario usuario;

    public ListaComLigeraDTO(Long id, Date created, Date modified, BigDecimal precio, Usuario usuario) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.precio = precio;
        this.usuario = usuario;
    }

    public ListaComLigeraDTO() {
    }

    public Long getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
