package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juanboris.springboot.backend.listaCompra.models.DTO.ProductoUltimoPrecioDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.RecetaLigeraDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.RecetaProductoDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.RecetaProductoProjectionDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.RecetaProjectionDTO;
import com.juanboris.springboot.backend.listaCompra.models.DTO.TipoProductoProjectionDTO;
import com.juanboris.springboot.backend.listaCompra.models.dao.IRecetaDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;
import com.juanboris.springboot.backend.listaCompra.models.entity.TipoProducto;

@Service
public class RecetaServiceImpl implements IRecetaService {

  @Autowired
  private IRecetaDAO recetaDAO;

  @Override
  @Transactional(readOnly = true)
  public List<Receta> findAll() {
    return (List<Receta>) recetaDAO.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<RecetaLigeraDTO> findAllByUsuarioId(Long usuarioId) {
    List<RecetaLigeraDTO> recetasDTO = new ArrayList<>();
    for (RecetaProjectionDTO recetaProjection : recetaDAO.findProjectedByUsuarioId(usuarioId)) {
      RecetaLigeraDTO recetaDTO = new RecetaLigeraDTO();
      recetaDTO.setRecetaId(recetaProjection.getRecetaId());
      recetaDTO.setNombre(recetaProjection.getNombre());
      recetaDTO.setDescripcion(recetaProjection.getDescripcion());
      recetaDTO.setVideoURL(recetaProjection.getVideoURL());
      recetaDTO.setTiempo(recetaProjection.getTiempo());
      recetaDTO.setImagen(recetaProjection.getImagen());
      recetaDTO.setCreated(recetaProjection.getCreated());
      recetaDTO.setModified(recetaProjection.getModified());
      recetaDTO.setUsuario(recetaProjection.getUsuario());

      Set<RecetaProductoDTO> productosDTO = new HashSet<>();
      for (RecetaProductoProjectionDTO productoProjection : recetaProjection.getProductos()) {
        ProductoUltimoPrecioDTO productoDTO = new ProductoUltimoPrecioDTO();
        productoDTO.setId(productoProjection.getProducto().getId());
        productoDTO.setNombre(productoProjection.getProducto().getNombre());
        productoDTO.setMarca(productoProjection.getProducto().getMarca());
        productoDTO.setMediaPrecio(Objects.toString(productoProjection.getProducto().getMediaPrecio(), ""));
        productoDTO.setUltimoPrecio(Objects.toString(productoProjection.getProducto().getPrecio(), ""));
        TipoProductoProjectionDTO tipoProjection = productoProjection.getProducto().getTipo();
        productoDTO.setTipo(tipoProjection != null
            ? new TipoProducto(tipoProjection.getId(), tipoProjection.getNombre(), tipoProjection.getColor())
            : null);

        productosDTO.add(new RecetaProductoDTO(productoProjection.getId(), productoDTO,
            productoProjection.getCantidad()));
      }
      recetaDTO.setProductos(productosDTO);

      recetasDTO.add(recetaDTO);
    }
    return recetasDTO;
  }

  @Override
  @Transactional
  public Receta save(Receta receta) {
    return recetaDAO.save(receta);
  }

  @Override
  @Transactional(readOnly = true)
  public Receta findById(Long id) {
    return recetaDAO.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public void delete(Receta receta) {
    recetaDAO.delete(receta);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Receta> findByNombreAndUser(String term, Long usuario) {

    return recetaDAO.findByNombreAndUser(term, usuario);
  }

  @Override
  @Transactional
  public void deleteProducto(Long idProducto, Long idReceta) {
    recetaDAO.deleteProducto(idProducto, idReceta);
  }
}
