package com.juanboris.springboot.backend.listaCompra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.juanboris.springboot.backend.listaCompra.auth",
    "com.juanboris.springboot.backend.listaCompra.controllers",
    "com.juanboris.springboot.backend.listaCompra.models.services"})
@EnableJpaRepositories(basePackages = "com.juanboris.springboot.backend.listaCompra.models.dao")
@EntityScan(basePackages = "com.juanboris.springboot.backend.listaCompra.models.entity")
public class ListaCompraApplication {

  public static void main(String[] args) {
    SpringApplication.run(ListaCompraApplication.class, args);
  }
}
