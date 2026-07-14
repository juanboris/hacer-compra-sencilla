package com.juanboris.springboot.backend.listaCompra.models.DTO;

import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class RecetaLigeraDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long recetaId;
    private String nombre;
    private String descripcion;
    private String videoURL;
    private int tiempo;
    private String imagen;
    private Date created;
    private Date modified;
    private Usuario usuario;
    private Set<RecetaProductoDTO> productos;

    public RecetaLigeraDTO() {
    }

    public Long getRecetaId() {
        return recetaId;
    }

    public void setRecetaId(Long recetaId) {
        this.recetaId = recetaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<RecetaProductoDTO> getProductos() {
        return productos;
    }

    public void setProductos(Set<RecetaProductoDTO> productos) {
        this.productos = productos;
    }
}
