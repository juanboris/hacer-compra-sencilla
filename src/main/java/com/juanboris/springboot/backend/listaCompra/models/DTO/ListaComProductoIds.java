package com.juanboris.springboot.backend.listaCompra.models.DTO;

import java.io.Serializable;

public class ListaComProductoIds implements Serializable {

  private static final long serialVersionUID = 1L;
  Long idLista;
  Long idProducto;

  public Long getIdLista() {
    return idLista;
  }

  public void setIdLista(Long idLista) {
    this.idLista = idLista;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public ListaComProductoIds(Long idLista, Long idProducto) {
    this.idLista = idLista;
    this.idProducto = idProducto;
  }


}
