package com.juanboris.springboot.backend.listaCompra.models.DTO;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import com.juanboris.springboot.backend.listaCompra.models.entity.TipoProducto;

public class ProductoDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private String nombre;
  private String precio;
  private String marca;
  private TipoProducto tipo;

  public ProductoDTO() {}

  public ProductoDTO(Long id, String nombre, String precio, String marca, TipoProducto tipo) {
    this.id = id;
    this.nombre = nombre;
    this.precio = precio;
    this.marca = marca;
    this.tipo = tipo;
  }

  @NotEmpty
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @Pattern(regexp = "^[0-9]{1,4}((\\.|\\,)[0-9]{1,2})?$",
      message = "tiene que ser un número con entre 1 y 4 enteros y 0 a 2 decimales")
  public String getPrecio() {
    return precio;
  }

  public void setPrecio(String precio) {
    this.precio = precio;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public TipoProducto getTipo() {
    return tipo;
  }

  public void setTipo(TipoProducto tipo) {
    this.tipo = tipo;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
