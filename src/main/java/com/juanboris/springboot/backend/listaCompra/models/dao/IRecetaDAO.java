package com.juanboris.springboot.backend.listaCompra.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;

public interface IRecetaDAO extends CrudRepository<Receta, Long> {
  @Query(
      value = "SELECT * FROM Recetas p WHERE LOWER(p.nombre) LIKE LOWER(:term) AND p.usuario_id=:user",
      nativeQuery = true)
  public List<Receta> findByNombreAndUser(@Param("term") String term, @Param("user") Long usuario);

  @Modifying
  @Query(
      value = "DELETE FROM producto_receta p WHERE p.producto_id=:productoId AND p.receta_id=:recetaId",
      nativeQuery = true)
  public void deleteProducto(@Param("productoId") Long idProducto,
      @Param("recetaId") Long idReceta);

}
