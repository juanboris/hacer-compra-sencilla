package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductoRecetaId implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long recetaId;
  private Long productoId;

  public ProductoRecetaId() {}

  public ProductoRecetaId(Long productoId, Long recetaId) {
    this.productoId = productoId;
    this.recetaId = recetaId;
  }

  @Column(name = "producto_id", nullable = false)
  public Long getProductoId() {
    return productoId;
  }

  public void setProductoId(Long productoId) {
    this.productoId = productoId;
  }

  @Column(name = "receta_id")
  public Long getRecetaId() {
    return recetaId;
  }

  public void setRecetaId(Long recetaId) {
    this.recetaId = recetaId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((productoId == null) ? 0 : productoId.hashCode());
    result = prime * result + ((recetaId == null) ? 0 : recetaId.hashCode());
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
    ProductoRecetaId other = (ProductoRecetaId) obj;
    if (productoId == null) {
      if (other.productoId != null)
        return false;
    } else if (!productoId.equals(other.productoId))
      return false;
    if (recetaId == null) {
      if (other.recetaId != null)
        return false;
    } else if (!recetaId.equals(other.recetaId))
      return false;
    return true;
  }
}
