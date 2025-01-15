package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juanboris.springboot.backend.listaCompra.models.dao.ICalendarioDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.RecetaCalendario;

@Service
@Lazy
public class CalendarioServiceImpl implements ICalendarioService {

  @Autowired
  private ICalendarioDAO calendarioDAO;

  @Override
  public List<RecetaCalendario> findAll() {
    return (List<RecetaCalendario>) calendarioDAO.findAll();
  }

  @Override
  @Transactional
  public RecetaCalendario save(RecetaCalendario calendario) {
    return calendarioDAO.save(calendario);
  }

  @Override
  @Transactional(readOnly = true)
  public RecetaCalendario findById(Long id) {
    return calendarioDAO.findById(id).orElse(null);
  }

  @Override
  public void delete(RecetaCalendario calendario) {
    calendarioDAO.delete(calendario);

  }

}
