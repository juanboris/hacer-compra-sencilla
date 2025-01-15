package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juanboris.springboot.backend.listaCompra.metodosAux.MetodosAux;

@Entity
@Table(name = "recetas")
public class Receta implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long recetaId;
  private String nombre;
  private String descripcion;
  private String videoURL;
  private int tiempo;
  private Date created;
  private Date modified;
  private String imagen;
  private Set<ProductoReceta> productos = new HashSet<ProductoReceta>(0);
  private Usuario usuario;

  public Receta(Long id) {
    this.recetaId = id;
  }

  public Receta() {}

  public Receta(Long recetaId, String nombre, String descripcion, String videoURL, int tiempo,
      Date created, Date modified, String imagen, Set<ProductoReceta> productos, Usuario usuario) {
    super();
    this.recetaId = recetaId;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.videoURL = videoURL;
    this.tiempo = tiempo;
    this.created = created;
    this.modified = modified;
    this.imagen = imagen;
    this.productos = productos;
    this.usuario = usuario;
  }

  @Id
  @Column(name = "id", unique = true, nullable = false)
  public Long getRecetaId() {
    return recetaId;
  }

  public void setRecetaId(Long recetaId) {
    this.recetaId = recetaId;
  }

  @NotEmpty
  @Column(name = "nombre", nullable = false)
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @Column(name = "modified")
  @Temporal(TemporalType.TIMESTAMP)
  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  @Column(name = "created")
  @Temporal(TemporalType.TIMESTAMP)
  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL)
  @JsonIgnoreProperties(value = {"receta"}, allowSetters = true)
  public Set<@Valid ProductoReceta> getProductos() {
    return productos;
  }

  public void setProductos(Set<ProductoReceta> productos) {
    this.productos = productos;
  }

  @Column(name = "tiempo")
  public int getTiempo() {
    return tiempo;
  }

  public void setTiempo(int tiempo) {
    this.tiempo = tiempo;
  }

  @Column(name = "descripcion", columnDefinition = "TEXT")
  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  @Column(name = "videourl")
  @URL
  public String getVideoURL() {
    return videoURL;
  }

  public void setVideoURL(String videoURL) {
    this.videoURL = videoURL;
  }

  public String getImagen() {
    return imagen;
  }

  public void setImagen(String imagen) {
    this.imagen = imagen;
  }

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  @Override
  public String toString() {
    return "Receta [id=" + recetaId + ", nombre=" + nombre + ", created=" + created + ", productos="
        + productos + "]";
  }

  /**
   * Método que se ejecuta cuando se inserta un registro y que calcula los campos created y modified
   * a partir de un método que les otorga la fecha correspondiente
   */
  @PrePersist
  public void prePersist() {
    try {
      created = MetodosAux.crearFecha();
      setModified(MetodosAux.crearFecha());
    } catch (ParseException e) {
      System.out.println("Fallo al formatear la fecha");
      e.printStackTrace();
    }
  }

  /**
   * Método que se ejecuta cuando el objeto se modifica y que establece el valor modified
   */
  @PreUpdate
  public void onPreUpdate() {
    try {
      setModified(MetodosAux.crearFecha());
    } catch (ParseException e) {
      System.out.println("Fallo al formatear la fecha");
      e.printStackTrace();
    }
  }

}
