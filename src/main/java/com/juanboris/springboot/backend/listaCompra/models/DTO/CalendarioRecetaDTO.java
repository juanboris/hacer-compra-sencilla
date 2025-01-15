package com.juanboris.springboot.backend.listaCompra.models.DTO;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class CalendarioRecetaDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private String name;
  private OffsetDateTime created;

  public CalendarioRecetaDTO() {}

  public CalendarioRecetaDTO(Long id, String name, OffsetDateTime created) {
    super();
    this.id = id;
    this.name = name;
    this.created = created;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

}
