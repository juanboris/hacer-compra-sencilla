package com.juanboris.springboot.backend.listaCompra.auth;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * UserDetails que además lleva el id de Usuario, para que InfoAdicionalToken no tenga que
 * volver a consultar la base de datos tras el login para obtenerlo.
 */
public class UsuarioDetails extends User {

  private static final long serialVersionUID = 1L;

  private final Long id;

  public UsuarioDetails(Long id, String username, String password, boolean enabled,
      boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired,
        accountNonLocked, authorities);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
