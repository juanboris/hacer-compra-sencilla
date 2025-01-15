package com.juanboris.springboot.backend.listaCompra.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

public interface IUsuarioDAO extends CrudRepository<Usuario, Long> {
  public Usuario findByUsername(String username);
}
