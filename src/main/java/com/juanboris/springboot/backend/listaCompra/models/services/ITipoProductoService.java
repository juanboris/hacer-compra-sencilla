package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import com.juanboris.springboot.backend.listaCompra.models.entity.TipoProducto;

public interface ITipoProductoService {
  public List<TipoProducto> findAll();

  public TipoProducto findById(Long id);
}
