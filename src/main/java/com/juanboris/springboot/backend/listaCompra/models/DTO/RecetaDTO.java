package com.juanboris.springboot.backend.listaCompra.models.DTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.juanboris.springboot.backend.listaCompra.models.entity.ProductoReceta;

public class RecetaDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private String nombre;
  private Set<ProductoReceta> productos = new HashSet<ProductoReceta>(0);

  public RecetaDTO() {}

  public RecetaDTO(Long id, String nombre, Set<ProductoReceta> productos) {
    super();
    this.id = id;
    this.nombre = nombre;
    this.productos = productos;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Set<ProductoReceta> getProductos() {
    return productos;
  }

  public void setProductos(Set<ProductoReceta> productos) {
    this.productos = productos;
  }
}
