package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class ListComLigeraProductsDetailedDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private BigDecimal precio;
    private Date created;
    private Set<ListaComProductsDetailedDTO> productos;

    public ListComLigeraProductsDetailedDTO(Long id, BigDecimal precio, Date created, Set<ListaComProductsDetailedDTO> productos) {
        this.id = id;
        this.precio = precio;
        this.created = created;
        this.productos = productos;
    }

    public ListComLigeraProductsDetailedDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<ListaComProductsDetailedDTO> getProductos() {
        return productos;
    }
    public void setProductos(Set<ListaComProductsDetailedDTO> productos) {
        this.productos = productos;
    }
}
