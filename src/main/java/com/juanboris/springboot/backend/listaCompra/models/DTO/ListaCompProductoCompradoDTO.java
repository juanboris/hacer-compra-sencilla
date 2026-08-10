package com.juanboris.springboot.backend.listaCompra.models.DTO;

import javax.validation.constraints.NotNull;

public class ListaCompProductoCompradoDTO {

  @NotNull
  private Boolean comprado;

  public Boolean getComprado() {
    return comprado;
  }

  public void setComprado(Boolean comprado) {
    this.comprado = comprado;
  }
}
