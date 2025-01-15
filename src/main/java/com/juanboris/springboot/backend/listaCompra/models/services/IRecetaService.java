package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;

public interface IRecetaService {
  public List<Receta> findAll();

  public Receta save(Receta receta);

  public Receta findById(Long id);

  public void delete(Receta receta);

  public List<Receta> findByNombreAndUser(String term, Long usuario);

  public void deleteProducto(Long idProducto, Long idReceta);
}
