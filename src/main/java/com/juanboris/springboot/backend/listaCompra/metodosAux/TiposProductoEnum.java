package com.juanboris.springboot.backend.listaCompra.metodosAux;

public enum TiposProductoEnum {
  VERDURA("Verdura"), FRUTA("Fruta"), CARNE("Carne"), PESCADO("Pescado"), PROCESADOS(
      "Procesado"), LEGUMBRE("Legumbres"), BEBIDA(
          "Bebida"), LACTEO("Lácteo"), BANYO("Baño"), PASTA("Pasta"), CEREALES("Cereales"),
  LIMPIEZA ("Limpieza"), FARMACIA ("Medicina"), FRUTOS_SECOS ("Frutos secos"), FERRETERIA ("Ferretería"),
  ESPECIAS ("Especias"), DULCE ("Dulce"), CUIDADO_PERSONAL ("Cuidado personal"),
  PLANTAS ("Plantas");

  private final String tipoProducto;

  private TiposProductoEnum(String tipoProducto) {
    this.tipoProducto = tipoProducto;
  }

  public String getTipoProducto() {
    return tipoProducto;
  }
}
