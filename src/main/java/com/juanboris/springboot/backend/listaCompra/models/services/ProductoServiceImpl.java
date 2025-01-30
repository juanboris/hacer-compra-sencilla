package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juanboris.springboot.backend.listaCompra.models.dao.IProductoDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;

@Service
public class ProductoServiceImpl implements IProductoService {
  @Autowired
  private IProductoDAO productoDAO;

  @Override
  @Transactional(readOnly = true)
  public List<Producto> findAll() {
    return (List<Producto>) productoDAO.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Producto> findAll(Pageable pageable) {
    return productoDAO.findAll(pageable);
  }

  @Override
  @Transactional
  public Producto save(Producto producto) {
    return productoDAO.save(producto);
  }

  @Override
  @Transactional(readOnly = true)
  public Producto findById(Long id) {
    return productoDAO.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public void delete(Producto producto) {
    productoDAO.delete(producto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Producto> findByNombreAndUsername(String term, Long usuario) {

    return productoDAO.findByNombreAndUsername(term, usuario);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Producto> findAllByUsuarioIdEquals(Pageable pageable, Long id, String nombre) {

    return productoDAO.findAllByUsuarioIdEqualsAndNombreStartsWithIgnoreCase(pageable, id, nombre);
  }
}
