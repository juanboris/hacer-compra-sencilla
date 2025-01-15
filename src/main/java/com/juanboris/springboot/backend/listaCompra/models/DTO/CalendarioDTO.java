package com.juanboris.springboot.backend.listaCompra.models.DTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CalendarioDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private Set<CalendarioRecetaDTO> calendariosRecetas = new HashSet<CalendarioRecetaDTO>(0);

  public CalendarioDTO(Set<CalendarioRecetaDTO> calendariosRecetas) {
    this.calendariosRecetas = calendariosRecetas;
  }

  public CalendarioDTO() {}

  public Set<CalendarioRecetaDTO> getCalendariosRecetas() {
    return calendariosRecetas;
  }

  public void setCalendariosRecetas(Set<CalendarioRecetaDTO> calendariosRecetas) {
    this.calendariosRecetas = calendariosRecetas;
  }

}
