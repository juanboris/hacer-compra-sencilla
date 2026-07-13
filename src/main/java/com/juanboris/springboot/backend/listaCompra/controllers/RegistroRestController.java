package com.juanboris.springboot.backend.listaCompra.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.metodosAux.NegativeNumberException;
import com.juanboris.springboot.backend.listaCompra.models.entity.Usuario;
import com.juanboris.springboot.backend.listaCompra.models.services.IUsuarioService;

@CrossOrigin(origins = {GeneralConstants.URL_CONNECTION})
@RestController
@RequestMapping("/api")
@Lazy
public class RegistroRestController {

  @Autowired
  private IUsuarioService iUsuarioService;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @PostMapping("/registro")
  public ResponseEntity<?> createUsuario(@Valid @RequestBody Usuario usuario,
      BindingResult result) {
    boolean encontrado = false;
    Map<String, Object> response = new HashMap<>();

    encontrado = comprobarUsuario(usuario.getUsername());

    if (encontrado) {
      response.put(GeneralConstants.MENSAJE, "El usuario ya existe en la base de datos");
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    try {
      usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
      usuario.setEnabled(true);

      if (result.hasErrors()) {
        List<String> errors = result.getFieldErrors().stream()
            .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
            .collect(Collectors.toList());
        response.put(GeneralConstants.ERRORS_STRING, errors);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
      }

      this.iUsuarioService.save(usuario);
    } catch (DataAccessException e) {
      response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_INSERT_BBDD);
      response.put(GeneralConstants.ERROR,
          e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (NegativeNumberException e) {
      response.put(GeneralConstants.MENSAJE, GeneralConstants.FALLO_INSERT_BBDD);
      response.put(GeneralConstants.ERROR, e.getMessage());
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put(GeneralConstants.MENSAJE, "¡El usuario ha sido creado con éxito!");
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  private boolean comprobarUsuario(String usuario) {
    boolean encontrado = false;

    for (Usuario usur : iUsuarioService.findAll()) {
      if (usur.getUsername().equalsIgnoreCase(usuario)) {
        encontrado = true;
      }
    }

    return encontrado;
  }
}
