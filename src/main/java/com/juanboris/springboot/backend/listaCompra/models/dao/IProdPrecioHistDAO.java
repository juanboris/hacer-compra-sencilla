package com.juanboris.springboot.backend.listaCompra.models.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.juanboris.springboot.backend.listaCompra.models.entity.ProducHistoricosFecha;
import com.juanboris.springboot.backend.listaCompra.models.entity.Producto;

public interface IProdPrecioHistDAO extends JpaRepository<ProducHistoricosFecha, Long> {
	/*@Query(
		      value = "INSERT INTO producto_precios_historicos (created, precios_historicos, producto_id) VALUES (:creationDate, :precio, :productoId)",
		      nativeQuery = true)
		  public List<Producto> nativeSave(@Param("creationDate") LocalDateTime creationDate,
		      @Param("precio") BigDecimal precio, @Param("productoId") Long productoId);*/
	
	public List<ProducHistoricosFecha> findByProducto (Producto id);
}
