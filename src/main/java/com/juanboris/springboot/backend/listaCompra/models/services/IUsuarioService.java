package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

public interface IUsuarioService {
  public Usuario findByUsername(String username);

  public List<Usuario> findAll();

  public Usuario save(Usuario usuario);
}
