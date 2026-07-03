package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ProductoProjectionDTO;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;

public interface IProductoService {
  public List<Producto> findAll();

  public Page<Producto> findAll(Pageable pageable);

  public Producto save(Producto producto);

  public Producto findById(Long id);

  public void delete(Producto producto);

  public List<Producto> findByNombreAndUsername(String term, Long usuario);

  public Page<Producto> findAllByUsuarioIdEquals(Pageable pageable, Long id, String nombre);

  public Page<ProductoProjectionDTO> findProjectedByUsuarioIdEquals(Pageable pageable, Long usuarioId, String nombre);
}
