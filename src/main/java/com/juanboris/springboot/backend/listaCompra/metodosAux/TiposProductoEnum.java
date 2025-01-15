package com.juanboris.springboot.backend.listaCompra.metodosAux;

public enum TiposProductoEnum {
  VERDURA("Verdura"), FRUTA("Fruta"), CARNE("Carne"), PESCADO("Pescado"), PROCESADOS(
      "Procesado"), LEGUMBRE("Legumbres"), BEBIDA(
          "Bebida"), LACTEO("Lácteo"), BANYO("Baño"), PASTA("Pasta"), CEREALES("Cereales");

  private final String tipoProducto;

  private TiposProductoEnum(String tipoProducto) {
    this.tipoProducto = tipoProducto;
  }

  public String getTipoProducto() {
    return tipoProducto;
  }
}
