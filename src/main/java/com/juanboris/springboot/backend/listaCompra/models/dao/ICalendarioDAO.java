package com.juanboris.springboot.backend.listaCompra.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juanboris.springboot.backend.listaCompra.models.entity.RecetaCalendario;

public interface ICalendarioDAO extends JpaRepository<RecetaCalendario, Long> {

}
