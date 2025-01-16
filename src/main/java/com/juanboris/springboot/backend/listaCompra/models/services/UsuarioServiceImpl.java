package com.juanboris.springboot.backend.listaCompra.models.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juanboris.springboot.backend.listaCompra.models.dao.IUsuarioDAO;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;

@Service
public class UsuarioServiceImpl implements UserDetailsService, IUsuarioService {

  private Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);
  @Autowired
  private IUsuarioDAO iUsuarioDAO;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario;

    Optional<String> usernameOptional = Optional.ofNullable(username);
    if (!usernameOptional.isPresent()) {
      usuarioNoEncontrado("desconocido");
    }
    Optional<Usuario> usuarioOptional =
        Optional.ofNullable(iUsuarioDAO.findByUsername(usernameOptional.get()));

    if (!usuarioOptional.isPresent()) {
      usuarioNoEncontrado(username);
    }
    usuario = usuarioOptional.get();
    List<GrantedAuthority> authorities =
        usuario.getRoles().stream().map(e -> new SimpleGrantedAuthority(e.getNombre()))
            .peek(e -> logger.info("Role: " + e.getAuthority())).collect(Collectors.toList());
    return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true,
        true, authorities);
  }

  @Override
  @Transactional(readOnly = true)
  public Usuario findByUsername(String username) {
    return iUsuarioDAO.findByUsername(username);

  }

  @Override
  public List<Usuario> findAll() {
    return (List<Usuario>) iUsuarioDAO.findAll();
  }

  @Override
  public Usuario save(Usuario usuario) {
    return iUsuarioDAO.save(usuario);
  }

  private void usuarioNoEncontrado(String username) {
    String mensaje = "Error en el login: no existe el usuario '" + username + "' en el sistema!";
    logger.error(mensaje);
    throw new UsernameNotFoundException(mensaje);
  }
}
