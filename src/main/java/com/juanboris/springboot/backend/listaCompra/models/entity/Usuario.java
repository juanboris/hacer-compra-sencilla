package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true, length = 20)
  @NotEmpty
  @Size(min = 3, max = 20, message = "debe tener entre 3 y 20 caracteres")
  @Pattern(regexp = "[0-9a-zA-Z]*", message = "no debe contener carácteres especiales")
  private String username;
  @Column(length = 60)
  @NotEmpty
  @Size(min = 3, max = 80, message = "debe tener entre 3 y 20 caracteres")
  private String password;
  private Boolean enabled;
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Rol> roles;

  public Usuario() {}

  public Usuario(Long id, String username, String password, Boolean enabled, List<Rol> roles) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.enabled = enabled;
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public List<Rol> getRoles() {
    return roles;
  }

  public void setRoles(List<Rol> roles) {
    this.roles = roles;
  }
}
