package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ProductoProjectionDTO;
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

  /*
   * El precio de un producto se muestra también dentro de las respuestas de listaComp
   * (ProductoUltimoPrecioDTO), así que cualquier guardado/borrado de producto invalida también
   * esa caché, no solo la de productos.
   */
  @Override
  @Transactional
  @Caching(evict = {@CacheEvict(value = GeneralConstants.CACHE_PRODUCTOS_PAGE, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_INDEX, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, allEntries = true)})
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
  @Caching(evict = {@CacheEvict(value = GeneralConstants.CACHE_PRODUCTOS_PAGE, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_INDEX, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, allEntries = true)})
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

  @Override
  @Transactional(readOnly = true)
  @Cacheable(GeneralConstants.CACHE_PRODUCTOS_PAGE)
  public Page<ProductoProjectionDTO> findProjectedByUsuarioIdEquals(Pageable pageable, Long usuarioId,
      String nombre) {
    return productoDAO.findProjectedByUsuarioIdEqualsAndNombreStartsWithIgnoreCase(pageable, usuarioId,
        nombre);
  }
}
