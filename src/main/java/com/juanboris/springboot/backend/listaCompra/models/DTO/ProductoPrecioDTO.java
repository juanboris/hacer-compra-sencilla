package com.juanboris.springboot.backend.listaCompra.models.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class ProductoPrecioDTO {

  @NotEmpty
  @Pattern(regexp = "^[0-9]{1,4}((\\.|\\,)[0-9]{1,2})?$", message = "tiene que ser un número con entre 1 y 4 enteros y 0 a 2 decimales")
  private String precio;

  public String getPrecio() {
    return precio;
  }

  public void setPrecio(String precio) {
    this.precio = precio;
  }
}
