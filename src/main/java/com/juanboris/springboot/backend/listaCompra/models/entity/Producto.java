package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juanboris.springboot.backend.listaCompra.metodosAux.MetodosAux;
import com.juanboris.springboot.backend.listaCompra.models.services.ProdPrecioHistService;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long productoId;
	private String nombre;
	private String precio;
	private String marca;
	private String tipo;
	private BigDecimal mediaPrecio;
	private Date created;
	private Date modified;
	private Set<ProductoReceta> recetas = new HashSet<ProductoReceta>();
	private Set<ListaCompProducto> listas = new HashSet<ListaCompProducto>();
	private List<ProducHistoricosFecha> preciosHistoricos = new ArrayList<ProducHistoricosFecha>();
	private Usuario usuario;

	public Producto() {
	}

	public Producto(Long productoId) {
		this.productoId = productoId;
	}

	public Producto(Long productoId, @NotEmpty String nombre, String precio, String marca, String tipo,
			BigDecimal mediaPrecio, Date created, Date modified, Set<ProductoReceta> recetas,
			Set<ListaCompProducto> listas, List<ProducHistoricosFecha> preciosHistoricos, Usuario usuario) {
		this.productoId = productoId;
		this.nombre = nombre;
		setPrecio(precio);
		this.marca = marca;
		this.tipo = tipo;
		this.mediaPrecio = mediaPrecio;
		this.created = created;
		this.modified = modified;
		this.recetas = recetas;
		this.listas = listas;
		this.preciosHistoricos = preciosHistoricos;
		this.usuario = usuario;
	}

	public Producto(String nombre, String precio, String marca, String tipo, Usuario usuario) {
		this.nombre = nombre;
		setPrecio(precio);
		this.marca = marca;
		this.tipo = tipo;
		this.usuario = usuario;
		this.mediaPrecio = Objects.nonNull(extractPrecio(precio)) ? new BigDecimal(extractPrecio(precio)) : null;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return productoId;
	}

	public void setId(Long id) {
		this.productoId = id;
	}

	@NotEmpty
	@Column(nullable = false)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@JsonIgnoreProperties(value = { "producto", "receta" }, allowSetters = true)
	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
	public Set<ProductoReceta> getRecetas() {
		return recetas;
	}

	public void setRecetas(Set<ProductoReceta> recetas) {
		this.recetas = recetas;
	}

	@JsonIgnoreProperties(value = { "producto", "listaCom" }, allowSetters = true)
	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
	public Set<ListaCompProducto> getListas() {
		return listas;
	}

	public void setListas(Set<ListaCompProducto> listas) {
		this.listas = listas;
	}

	@Pattern(regexp = "^[0-9]{1,4}((\\.|\\,)[0-9]{1,2})?$", message = "tiene que ser un número con entre 1 y 4 enteros y 0 a 2 decimales")
	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = extractPrecio(precio);
	}

	private String extractPrecio(String precio) {
		if (Optional.ofNullable(precio).isPresent()) {
			return precio.contains(",") ? precio.replace(",", ".") : precio;
		}
		return null;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getMediaPrecio() {
		return mediaPrecio;
	}

	public void setMediaPrecio(BigDecimal mediaPrecio) {
		this.mediaPrecio = mediaPrecio;
	}

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value = "producto", allowSetters = true)
	public List<ProducHistoricosFecha> getPreciosHistoricos() {
		return preciosHistoricos;
	}

	public void setPreciosHistoricos(List<ProducHistoricosFecha> preciosHistoricos) {
		this.preciosHistoricos = preciosHistoricos;
	}

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Método que se ejecuta cuando se inserta un registro y que calcula los campos
	 * created y modified a partir de un método que les otorga la fecha
	 * correspondiente, añade el precio a la lista de precios y calcula la media de
	 * precios
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
	}

	/**
	 * Método que se ejecuta cuando el objeto se modifica y que establece el valor
	 * modified, añade el nuevo precio a la lista y calcula el precio medio
	 */
	@PreUpdate
	public void onPreUpdate() {
		try {
			modified = MetodosAux.crearFecha();
		} catch (ParseException e) {
			System.out.println("Fallo al formatear la fecha");
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Producto [id=" + productoId + ", nombre=" + nombre + ", created=" + created + ", recetas=" + recetas
				+ ", listas=" + listas + "]";
	}

	/**
	 * Método para calcular la media del producto a partir de los precios
	 * almacenados en la list preciosHistóricos
	 */
	private void calcularMedia() {

		if (this.preciosHistoricos != null && this.preciosHistoricos.size() > 0) {
			mediaPrecio = new BigDecimal(preciosHistoricos.stream()
					.mapToDouble(a -> a.getPreciosHistoricos().doubleValue()).average().getAsDouble());
		}
	}
}
