package com.juanboris.springboot.backend.listaCompra.models.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.models.DTO.*;
import com.juanboris.springboot.backend.listaCompra.models.dao.IUsuarioDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.ProducHistoricosFecha;
import com.juanboris.springboot.backend.listaCompra.models.entity.TipoProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    return mapListaComLigera(listaCompDAO.findAllProjectedBy());
  }

  /*
   * Igual que findAll(), pero filtrando por usuario en la propia consulta SQL en vez de traer
   * todas las listas de todos los usuarios y descartar la mayoría en memoria.
   */
  @Override
  @Transactional(readOnly = true)
  @Cacheable(GeneralConstants.CACHE_LISTA_COMP_INDEX)
  public List<ListaComLigeraDTO> findAllByUsuarioId(Long usuarioId) {
    return mapListaComLigera(listaCompDAO.findProjectedByUsuarioId(usuarioId));
  }

  private List<ListaComLigeraDTO> mapListaComLigera(List<ListaComLigeraProjectionDTO> listsProjection) {
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
  @Caching(evict = {@CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_INDEX, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, allEntries = true)})
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
  @Caching(evict = {@CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_INDEX, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, allEntries = true)})
  public void delete(ListaCom listaCom) {
    listaCompDAO.delete(listaCom);
  }

  @Override
  @Transactional
  @Caching(evict = {@CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_INDEX, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, allEntries = true)})
  public void deleteProducto(Long idProducto, Long idLista) {
    listaCompDAO.deleteProducto(idProducto, idLista);
  }

  @Override
  @Transactional
  @Caching(evict = {@CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_INDEX, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, allEntries = true)})
  public void deleteProductosComprados(Long idLista) {
    listaCompDAO.deleteProductosComprados(idLista);
  }

  @Override
  @Transactional
  @Caching(evict = {@CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_INDEX, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, allEntries = true)})
  public int actualizarComprado(Long idLista, Long idProducto, Boolean comprado) {
    return listaCompDAO.actualizarComprado(idLista, idProducto, comprado);
  }

  @Override
  @Caching(evict = {@CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_INDEX, allEntries = true),
      @CacheEvict(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, allEntries = true)})
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
  @Cacheable(value = GeneralConstants.CACHE_LISTA_COMP_BY_ID, key = "#id")
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
        TipoProductoProjectionDTO tipoProjection = listComProduct.getProducto().getTipo();
        productoUltimoPrecioDTO.setTipo(tipoProjection != null
            ? new TipoProducto(tipoProjection.getId(), tipoProjection.getNombre(), tipoProjection.getColor())
            : null);
        productoUltimoPrecioDTO.setUltimoPrecio(Objects.toString(listComProduct.getProducto().getPrecio(), ""));
        listaComProductsDetailedDTO.setProducto(productoUltimoPrecioDTO);

        listaComProductsDetailedDTOS.add(listaComProductsDetailedDTO);
      }
      ligeraProductsDetailedDTO.setProductos(listaComProductsDetailedDTOS);

      Set<ListaComProductsDetailedDTO> productosOrdenados = getListaComProductsDetailedDTOS();
      productosOrdenados.addAll(ligeraProductsDetailedDTO.getProductos());
      ligeraProductsDetailedDTO.setProductos(productosOrdenados);
      return ligeraProductsDetailedDTO;
    }

    return null;
  }

  private static Set<ListaComProductsDetailedDTO> getListaComProductsDetailedDTOS() {
    Set<ListaComProductsDetailedDTO> productosOrdenados = new TreeSet<>(new Comparator<ListaComProductsDetailedDTO>() {
      @Override
      public int compare(ListaComProductsDetailedDTO o1, ListaComProductsDetailedDTO o2) {
        // Ordenamos por la propiedad booleana, primero true (1) y luego false (0)
        Boolean p1 = o1.getComprado() != null ? o1.getComprado() : false;
        Boolean p2 = o2.getComprado() != null ? o2.getComprado() : false;

        // Ordenamos por la propiedad booleana: primero true (1), luego false (0)
        int result = Boolean.compare(p1, p2);

        if (result != 0) {
          // Si son iguales en la propiedad 'comprado', comparamos por ID para garantizar unicidad
          return result;
        }

        String tipo1 = (o1.getProducto() != null && o1.getProducto().getTipo() != null) ? o1.getProducto().getTipo().getNombre() : "";
        String tipo2 = (o2.getProducto() != null && o2.getProducto().getTipo() != null) ? o2.getProducto().getTipo().getNombre() : "";
        result = tipo1.compareTo(tipo2);
        if (result != 0)
        {
          return result;
        }
        return o1.getId().compareTo(o2.getId());
      }
    });
    return productosOrdenados;
  }

  @Override
  public ListComLigeraProductsDetailedDTO mapEntityToDTO(ListaCom listaCom) {
    ListComLigeraProductsDetailedDTO listComLigeraProductsDetailedDTO = new ListComLigeraProductsDetailedDTO();
    listComLigeraProductsDetailedDTO.setId(listaCom.getId());
    listComLigeraProductsDetailedDTO.setPrecio(listaCom.getPrecio());
    listComLigeraProductsDetailedDTO.setCreated(listaCom.getCreated());
    Set<ListaComProductsDetailedDTO> listaComProductsDetailedDTOS = getListaComProductsDetailedDTOS();
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
