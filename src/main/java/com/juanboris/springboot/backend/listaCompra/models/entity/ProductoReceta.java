package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "producto_receta")
public class ProductoReceta implements Serializable {

  private static final long serialVersionUID = 1L;

  private ProductoRecetaId id;
  private Receta receta;
  private Producto producto;
  private String cantidad;

  public ProductoReceta() {}

  public ProductoReceta(ProductoRecetaId id, Receta receta, Producto producto, String cantidad) {
    this.id = id;
    this.receta = receta;
    this.producto = producto;
    setCantidad(cantidad);
  }

  @EmbeddedId
  public ProductoRecetaId getId() {
    return id;
  }

  public void setId(ProductoRecetaId id) {
    this.id = id;
  }

  @ManyToOne
  @JoinColumn(name = "producto_id", insertable = false, updatable = false)
  public Producto getProducto() {
    return producto;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  @ManyToOne
  @JoinColumn(name = "receta_id", insertable = false, updatable = false)
  public Receta getReceta() {
    return receta;
  }

  public void setReceta(Receta receta) {
    this.receta = receta;
  }

  @Pattern(regexp = "^[1-9]\\d{0,1}$",
      message = "solo puede tener números enteros y positivos de dos cifras")
  @Column(name = "cantidad", nullable = false)
  public String getCantidad() {
    return cantidad;
  }

  public void setCantidad(String cantidad) {
    this.cantidad = cantidad;
  }

  @Override
  public String toString() {
    return "ProductoReceta [id=" + id + ", producto=" + producto + ", receta=" + receta
        + ", cantidad=" + cantidad + "]";
  }

}
