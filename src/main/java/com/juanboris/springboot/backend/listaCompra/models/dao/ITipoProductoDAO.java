package com.juanboris.springboot.backend.listaCompra.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.juanboris.springboot.backend.listaCompra.models.entity.TipoProducto;

public interface ITipoProductoDAO extends JpaRepository<TipoProducto, Long> {
}
