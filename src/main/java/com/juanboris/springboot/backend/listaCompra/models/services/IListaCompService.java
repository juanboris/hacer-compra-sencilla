package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;

import com.juanboris.springboot.backend.listaCompra.models.DTO.ListComLigeraProductsDetailedDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ListaComLigeraDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCom;
import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCompProducto;

public interface IListaCompService {
	public List<ListaComLigeraDTO> findAll();

	public List<ListaComLigeraDTO> findAllByUsuarioId(Long usuarioId);

	public Page<ListaCom> findAll(Pageable pageable);

	public ListaCom save(ListaCom listaCom);

	public ListaCom findById(Long id);

	public void delete(ListaCom listaCom);

	public void deleteProducto(Long idProducto, Long idLista);

	public void deleteProductosComprados(Long idLista);

	public int actualizarComprado(Long idLista, Long idProducto, Boolean comprado);

	public void anyadirProductoLista(Long idLista, Long idProducto, String cantidad);

	public List<ListaCompProducto> obtenerProductosLista(Long idLista);

	public ListComLigeraProductsDetailedDTO findByIdLazy(Long id);

	public ListComLigeraProductsDetailedDTO mapEntityToDTO(ListaCom listaCom);
}
