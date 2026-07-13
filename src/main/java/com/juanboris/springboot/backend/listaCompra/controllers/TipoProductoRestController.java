package com.juanboris.springboot.backend.listaCompra.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.juanboris.springboot.backend.listaCompra.metodosAux.GeneralConstants;
import com.juanboris.springboot.backend.listaCompra.models.entity.TipoProducto;
import com.juanboris.springboot.backend.listaCompra.models.services.ITipoProductoService;

@CrossOrigin(origins = {GeneralConstants.URL_CONNECTION})
@RestController
@RequestMapping("/api")
public class TipoProductoRestController {

  @Autowired
  private ITipoProductoService iTipoProductoService;

  @GetMapping("/tiposProducto")
  public List<TipoProducto> index() {
    return iTipoProductoService.findAll();
  }
}
