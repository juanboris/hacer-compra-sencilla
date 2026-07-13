package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juanboris.springboot.backend.listaCompra.metodosAux.MetodosAux;

@Entity
@Table(name = "lista_com")
public class ListaCom implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private Date created;
  private Date modified;
  private BigDecimal precio;
  private Set<ListaCompProducto> productos = new HashSet<ListaCompProducto>();
  private Usuario usuario;

  public ListaCom(Long id) {
    this.id = id;
  }

  public ListaCom() {}

  public ListaCom(Long id, Date created, Date modified, BigDecimal precio,
      Set<ListaCompProducto> productos, Usuario usuario) {
    this.id = id;
    this.created = created;
    this.modified = modified;
    this.precio = precio;
    this.productos = productos;
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

  @Column
  @Temporal(TemporalType.DATE)
  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @Column
  @Temporal(TemporalType.DATE)
  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  @Column
  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    if (precio == null) {
      precio = BigDecimal.valueOf(0D);
    }
    this.precio = precio;
  }

  @OneToMany(mappedBy = "listaCom", cascade = CascadeType.ALL)
  @JsonIgnoreProperties(value = {"listaCom"}, allowSetters = true)
  public Set<@Valid ListaCompProducto> getProductos() {
    return productos;
  }

  public void setProductos(Set<ListaCompProducto> productos) {
    this.productos = productos;
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
    return "ListaCom [id=" + id + ", created=" + created + ", productos=" + productos + "]";
  }

  /**
   * Método que se ejecuta cuando se inserta un registro y que calcula los campos created y modified
   * a partir de un método que les otorga la fecha correspondiente, añade el precio a la lista de
   * precios y calcula la media de precios
   */
  @PrePersist
  public void prePersist() {
    try {
      created = MetodosAux.crearFecha();
      modified = MetodosAux.crearFecha();
    } catch (ParseException e) {
      System.out.println("Fallo al formatear la fecha");
      e.printStackTrace();
    }

    calcularPrecioTotal();
  }

  /**
   * Método que se ejecuta cuando el objeto se modifica y que establece el valor modified, añade el
   * nuevo precio a la lista y calcula el precio medio
   */
  @PreUpdate
  public void onPreUpdate() {
    try {
      modified = MetodosAux.crearFecha();
    } catch (ParseException e) {
      System.out.println("Fallo al formatear la fecha");
      e.printStackTrace();
    }
    calcularPrecioTotal();
  }

  /**
   * Método para calcular el precio total de la lista de la compra. Recorro los productos insertados
   * en la lista, obtengo su precio lo multiplico por la cantidad que hay en la lista, y lo sumo
   */
  private void calcularPrecioTotal() {
    Double precioTotalProv = 0d;
    BigDecimal precioTotal = null;

    if (productos != null && productos.size() > 0) {
      precioTotalProv =
          productos.stream().filter(productos -> productos.getProducto().getPrecio() != null)
              .mapToDouble(producto -> Double.valueOf(producto.getProducto().getPrecio())
                  * Double.valueOf(producto.getCantidad()))
              .sum();
    }

    precioTotal = new BigDecimal(precioTotalProv);
    precio = precioTotal;
  }
}
