package com.juanboris.springboot.backend.listaCompra.models.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.juanboris.springboot.backend.listaCompra.models.DTO.*;
import com.juanboris.springboot.backend.listaCompra.models.dao.IUsuarioDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.ProducHistoricosFecha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juanboris.springboot.backend.listaCompra.models.dao.IListaCompDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCom;
import com.juanboris.springboot.backend.listaCompra.models.entity.ListaCompProducto;

@Service
public class ListaCompServiceImpl implements IListaCompService {
  @Autowired
  private IListaCompDAO listaCompDAO;

  @Autowired
  private IUsuarioDAO iUsuarioDAO;

  @Override
  @Transactional(readOnly = true)
  public List<ListaComLigeraDTO> findAll() {
    List<ListaComLigeraProjectionDTO> listsProjection = listaCompDAO.findAllProjectedBy();
    List<ListaComLigeraDTO> listsDTO = new ArrayList<>();
    listsProjection.forEach(list -> {
      ListaComLigeraDTO listDTO = new ListaComLigeraDTO();
      listDTO.setId(list.getId());
      listDTO.setCreated(list.getCreated());
      listDTO.setModified(list.getModified());
      listDTO.setPrecio(list.getPrecio());
      listDTO.setUsuario(list.getUsuario());
      listsDTO.add(listDTO);
    });
    return listsDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ListaCom> findAll(Pageable pageable) {
    return listaCompDAO.findAll(pageable);
  }

  @Override
  @Transactional
  public ListaCom save(ListaCom listaCom) {
    return listaCompDAO.save(listaCom);
  }

  @Override
  @Transactional(readOnly = true)
  public ListaCom findById(Long id) {
    return listaCompDAO.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public void delete(ListaCom listaCom) {
    listaCompDAO.delete(listaCom);
  }

  @Override
  @Transactional
  public void deleteProducto(Long idProducto, Long idLista) {
    listaCompDAO.deleteProducto(idProducto, idLista);
  }

  @Override
  public void anyadirProductoLista(Long idLista, Long idProducto, String cantidad) {
    listaCompDAO.anyadirProductoLista(idProducto, idLista, cantidad);
  }

  @Override
  public List<ListaCompProducto> obtenerProductosLista(Long idLista) {
    BigInteger b = listaCompDAO.obtenerProductosLista(idLista);
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public ListComLigeraProductsDetailedDTO findByIdLazy(Long id) {
    ListComLigeraProductsDetailedProjectionDTO ligeraProductsDetailedProjectionDTO = listaCompDAO.findProjectedById(id).orElse(null);
    ListComLigeraProductsDetailedDTO ligeraProductsDetailedDTO = new ListComLigeraProductsDetailedDTO();
    if (ligeraProductsDetailedProjectionDTO != null) {
      ligeraProductsDetailedDTO.setId(ligeraProductsDetailedProjectionDTO.getId());
      ligeraProductsDetailedDTO.setPrecio(ligeraProductsDetailedProjectionDTO.getPrecio());
      ligeraProductsDetailedDTO.setCreated(ligeraProductsDetailedProjectionDTO.getCreated());
      Set<ListaComProductsDetailedDTO> listaComProductsDetailedDTOS = new HashSet<>();

      for (ListaComProductsDetailedProjectionDTO listComProduct : ligeraProductsDetailedProjectionDTO.getProductos()) {
        ListaComProductsDetailedDTO listaComProductsDetailedDTO = new ListaComProductsDetailedDTO();
        listaComProductsDetailedDTO.setId(listComProduct.getId());
        listaComProductsDetailedDTO.setCantidad(listComProduct.getCantidad());
        listaComProductsDetailedDTO.setComprado(listComProduct.getComprado());
        ProductoUltimoPrecioDTO productoUltimoPrecioDTO = new ProductoUltimoPrecioDTO();
        productoUltimoPrecioDTO.setId(listComProduct.getProducto().getId());
        productoUltimoPrecioDTO.setMarca(listComProduct.getProducto().getMarca());
        productoUltimoPrecioDTO.setNombre(listComProduct.getProducto().getNombre());
        productoUltimoPrecioDTO.setMediaPrecio(Objects.toString(listComProduct.getProducto().getMediaPrecio(), ""));
        productoUltimoPrecioDTO.setTipo(listComProduct.getProducto().getTipo());
        productoUltimoPrecioDTO.setUltimoPrecio(Objects.toString(listComProduct.getProducto().getPrecio(), ""));
        listaComProductsDetailedDTO.setProducto(productoUltimoPrecioDTO);

        listaComProductsDetailedDTOS.add(listaComProductsDetailedDTO);
      }
      ligeraProductsDetailedDTO.setProductos(listaComProductsDetailedDTOS);
      return ligeraProductsDetailedDTO;
    }

    return null;
  }

  @Override
  public ListComLigeraProductsDetailedDTO mapEntityToDTO(ListaCom listaCom) {
    ListComLigeraProductsDetailedDTO listComLigeraProductsDetailedDTO = new ListComLigeraProductsDetailedDTO();
    listComLigeraProductsDetailedDTO.setId(listaCom.getId());
    listComLigeraProductsDetailedDTO.setPrecio(listaCom.getPrecio());
    listComLigeraProductsDetailedDTO.setCreated(listaCom.getCreated());
    Set<ListaComProductsDetailedDTO> listaComProductsDetailedDTOS = new HashSet<>();
    for (ListaCompProducto listaCompProducto : listaCom.getProductos()) {
      ListaComProductsDetailedDTO listaComProductsDetailedDTO = new ListaComProductsDetailedDTO();
      listaComProductsDetailedDTO.setId(listaCompProducto.getId());
      listaComProductsDetailedDTO.setCantidad(listaCompProducto.getCantidad());
      listaComProductsDetailedDTO.setComprado(listaCompProducto.getComprado());
      ProductoUltimoPrecioDTO productoUltimoPrecioDTO = new ProductoUltimoPrecioDTO();
      productoUltimoPrecioDTO.setId(listaCompProducto.getProducto().getId());
      productoUltimoPrecioDTO.setMarca(listaCompProducto.getProducto().getMarca());
      productoUltimoPrecioDTO.setNombre(listaCompProducto.getProducto().getNombre());
      productoUltimoPrecioDTO.setMediaPrecio(Objects.toString(listaCompProducto.getProducto().getMediaPrecio(), ""));
      productoUltimoPrecioDTO.setTipo(listaCompProducto.getProducto().getTipo());
      productoUltimoPrecioDTO.setUltimoPrecio(Objects.toString(listaCompProducto.getProducto().getPrecio(), ""));
      listaComProductsDetailedDTO.setProducto(productoUltimoPrecioDTO);

      listaComProductsDetailedDTOS.add(listaComProductsDetailedDTO);
    }

    listComLigeraProductsDetailedDTO.setProductos(listaComProductsDetailedDTOS);

    return listComLigeraProductsDetailedDTO;
  }
}
