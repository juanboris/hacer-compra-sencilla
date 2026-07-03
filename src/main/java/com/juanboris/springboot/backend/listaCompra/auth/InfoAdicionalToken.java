package com.juanboris.springboot.backend.listaCompra.auth;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;
import com.juanboris.springboot.backend.listaCompra.models.services.IUsuarioService;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

  @Autowired
  private IUsuarioService usuarioService;

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
      OAuth2Authentication authentication) {
    Long id = obtenerIdSinConsultarBBDD(authentication);
    if (id == null) {
      // Fallback: solo si el principal no es del tipo esperado (p.ej. grant distinto a password/refresh_token)
      Usuario usuario = usuarioService.findByUsername(authentication.getName());
      id = usuario.getId();
    }
    Map<String, Object> info = new HashMap<>();
    info.put("nombre_usuario", id + ": " + authentication.getName());
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
    return accessToken;
  }

  /*
   * El id ya se cargó durante el login (loadUserByUsername). Lo reutilizamos desde el
   * principal en vez de volver a consultar la base de datos.
   */
  private Long obtenerIdSinConsultarBBDD(OAuth2Authentication authentication) {
    Authentication userAuthentication = authentication.getUserAuthentication();
    if (userAuthentication != null && userAuthentication.getPrincipal() instanceof UsuarioDetails) {
      return ((UsuarioDetails) userAuthentication.getPrincipal()).getId();
    }
    return null;
  }

}
