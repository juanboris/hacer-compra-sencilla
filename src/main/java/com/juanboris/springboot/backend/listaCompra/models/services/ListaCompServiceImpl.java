package com.juanboris.springboot.backend.listaCompra.models.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.juanboris.springboot.backend.listaCompra.models.DTO.ListaComLigeraDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ListaComLigeraProjectionDTO;
import com.juanboris.springboot.backend.listaCompra.models.dao.IUsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
}
