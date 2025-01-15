package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juanboris.springboot.backend.listaCompra.models.dao.IRecetaDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.Receta;

@Service
@Lazy
public class RecetaServiceImpl implements IRecetaService {

  @Autowired
  private IRecetaDAO recetaDAO;

  @Override
  @Transactional(readOnly = true)
  public List<Receta> findAll() {
    return (List<Receta>) recetaDAO.findAll();
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
