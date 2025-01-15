package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import com.juanboris.springboot.backend.listaCompra.models.entity.RecetaCalendario;

public interface ICalendarioService {

  public List<RecetaCalendario> findAll();

  public RecetaCalendario save(RecetaCalendario calendario);

  public RecetaCalendario findById(Long id);

  public void delete(RecetaCalendario calendario);
}
