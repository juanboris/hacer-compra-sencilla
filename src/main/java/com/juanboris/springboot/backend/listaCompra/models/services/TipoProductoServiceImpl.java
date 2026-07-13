package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juanboris.springboot.backend.listaCompra.models.dao.ITipoProductoDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.TipoProducto;

@Service
public class TipoProductoServiceImpl implements ITipoProductoService {

  @Autowired
  private ITipoProductoDAO tipoProductoDAO;

  @Override
  @Transactional(readOnly = true)
  public List<TipoProducto> findAll() {
    return tipoProductoDAO.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public TipoProducto findById(Long id) {
    return tipoProductoDAO.findById(id).orElse(null);
  }
}
