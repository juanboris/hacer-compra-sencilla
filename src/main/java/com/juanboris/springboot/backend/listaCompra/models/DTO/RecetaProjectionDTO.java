package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

import java.util.Date;
import java.util.Set;

public interface RecetaProjectionDTO {
    public Long getRecetaId();
    public String getNombre();
    public String getDescripcion();
    public String getVideoURL();
    public int getTiempo();
    public String getImagen();
    public Date getCreated();
    public Date getModified();
    public Usuario getUsuario();
    public Set<RecetaProductoProjectionDTO> getProductos();
}