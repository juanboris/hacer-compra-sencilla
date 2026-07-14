package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import com.juanboris.springboot.backend.listaCompra.models.DTO.RecetaLigeraDTO;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;

public interface IRecetaService {
  public List<Receta> findAll();

  /* Igual que findAll(), pero filtrando por usuario en la propia consulta SQL y devolviendo un DTO
   * liviano en vez de las entidades completas, para no disparar la carga de las colecciones LAZY
   * (recetas, listas, preciosHistoricos) de cada Producto durante la serialización JSON */
  public List<RecetaLigeraDTO> findAllByUsuarioId(Long usuarioId);

  public Receta save(Receta receta);

  public Receta findById(Long id);

  public void delete(Receta receta);

  public List<Receta> findByNombreAndUser(String term, Long usuario);

  public void deleteProducto(Long idProducto, Long idReceta);
}
