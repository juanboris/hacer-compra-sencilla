package com.juanboris.springboot.backend.listaCompra.models.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.juanboris.springboot.backend.listaCompra.models.DTO.ListComLigeraProductsDetailedProjectionDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ListaComLigeraProjectionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCom;

public interface IListaCompDAO extends JpaRepository<ListaCom, Long> {
	@Modifying
	@Query(value = "DELETE FROM lista_com_producto l WHERE l.producto_id=:productoId AND l.lista_comp_id=:listaId", nativeQuery = true)
	public void deleteProducto(@Param("productoId") Long idProducto, @Param("listaId") Long idLista);

	@Modifying
	@Query(value = "DELETE FROM lista_com_producto l WHERE l.lista_comp_id=:listaId AND l.comprado = true", nativeQuery = true)
	public void deleteProductosComprados(@Param("listaId") Long idLista);

	@Modifying
	@Query(value = "UPDATE lista_com_producto SET comprado = :comprado WHERE lista_comp_id = :listaId AND producto_id = :productoId", nativeQuery = true)
	public int actualizarComprado(@Param("listaId") Long idLista, @Param("productoId") Long idProducto,
			@Param("comprado") Boolean comprado);

	@Modifying
	@Query(value = "INSERT INTO lista_com_producto l (lista_comp_id, producto_id, cantidad) VALUES (listaId,productoId,cantidad)", nativeQuery = true)
	public void anyadirProductoLista(@Param("productoId") Long idProducto, @Param("listaId") Long idLista,
			@Param("cantidad") String cantidad);
	
	@Modifying
	@Query(value = "SELECT * FROM lista_com_producto l  WHERE l.lista_comp_id=:listaId", nativeQuery = true)
	public BigInteger obtenerProductosLista(@Param("listaId") Long idLista);

	List<ListaComLigeraProjectionDTO> findAllProjectedBy();

	/* Filtra por usuario en la propia consulta, en vez de traer las listas de todos los usuarios */
	List<ListaComLigeraProjectionDTO> findProjectedByUsuarioId(Long usuarioId);

	Optional<ListComLigeraProductsDetailedProjectionDTO> findProjectedById(Long id);
}
