package com.juanboris.springboot.backend.listaCompra.metodosAux;

public class NegativeNumberException extends RuntimeException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public NegativeNumberException(String errorMensaje) {
    super(errorMensaje);
  }

}
