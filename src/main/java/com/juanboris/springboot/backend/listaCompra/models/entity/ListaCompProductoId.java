package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ListaCompProductoId implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long listaCompId;
  private Long productoId;

  public ListaCompProductoId() {}

  public ListaCompProductoId(Long listaCompId, Long productoId) {
    this.listaCompId = listaCompId;
    this.productoId = productoId;
  }

  @Column(name = "producto_id", nullable = false)
  public Long getProductoId() {
    return productoId;
  }

  public void setProductoId(Long productoId) {
    this.productoId = productoId;
  }

  @Column(name = "lista_comp_id")
  public Long getListaCompId() {
    return listaCompId;
  }

  public void setListaCompId(Long listaCompId) {
    this.listaCompId = listaCompId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((listaCompId == null) ? 0 : listaCompId.hashCode());
    result = prime * result + ((productoId == null) ? 0 : productoId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ListaCompProductoId other = (ListaCompProductoId) obj;
    if (listaCompId == null) {
      if (other.listaCompId != null)
        return false;
    } else if (!listaCompId.equals(other.listaCompId))
      return false;
    if (productoId == null) {
      if (other.productoId != null)
        return false;
    } else if (!productoId.equals(other.productoId))
      return false;
    return true;
  }
}
