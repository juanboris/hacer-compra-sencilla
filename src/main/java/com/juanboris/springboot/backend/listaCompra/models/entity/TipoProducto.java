package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "tipos_producto")
public class TipoProducto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String nombre;
  private String color;

  public TipoProducto() {}

  public TipoProducto(Long id, String nombre, String color) {
    this.id = id;
    this.nombre = nombre;
    this.color = color;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotEmpty
  @Column(nullable = false, unique = true)
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @NotEmpty
  @Column(nullable = false)
  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }
}
