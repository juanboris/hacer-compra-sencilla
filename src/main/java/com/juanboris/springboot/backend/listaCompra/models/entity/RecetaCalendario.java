package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "receta_calendario")
public class RecetaCalendario implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private Receta receta;
  private OffsetDateTime created;
  private Usuario usuario;

  public RecetaCalendario() {}

  public RecetaCalendario(Long id, Receta receta, OffsetDateTime created, Usuario usuario) {
    this.id = id;
    this.receta = receta;
    this.created = created;
    this.usuario = usuario;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receta_id")
  public Receta getReceta() {
    return receta;
  }

  public void setReceta(Receta receta) {
    this.receta = receta;

  }

  @Column(name = "created")
  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
}
