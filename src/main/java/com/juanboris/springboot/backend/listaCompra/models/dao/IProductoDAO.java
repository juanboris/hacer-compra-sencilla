package com.juanboris.springboot.backend.listaCompra.models.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;

public interface IProductoDAO extends JpaRepository<Producto, Long> {

  @Query(
      value = "SELECT * FROM Productos p WHERE LOWER(unaccent(p.nombre)) LIKE LOWER(:term) AND p.usuario_id=:user",
      nativeQuery = true)
  public List<Producto> findByNombreAndUsername(@Param("term") String term,
      @Param("user") Long usuario);

  public Page<Producto> findAllByUsuarioIdEqualsAndNombreStartsWithIgnoreCase(Pageable pageable, Long name, String nombre);
}
