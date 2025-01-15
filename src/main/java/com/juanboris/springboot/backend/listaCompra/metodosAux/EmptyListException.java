package com.juanboris.springboot.backend.listaCompra.metodosAux;

public class EmptyListException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public EmptyListException(String mensaje) {
    super(mensaje);
  }
}
