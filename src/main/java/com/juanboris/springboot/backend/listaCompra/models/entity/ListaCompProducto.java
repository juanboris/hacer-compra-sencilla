package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "listaCom_producto")
public class ListaCompProducto implements Serializable {

	private static final long serialVersionUID = 1L;

	private ListaCompProductoId id;
	private ListaCom listaCom;
	private Producto producto;
	private String cantidad;
	private Boolean comprado;

	public ListaCompProducto() {
	}

	public ListaCompProducto(ListaCompProductoId id, ListaCom listaCom, Producto producto, String cantidad,
			Boolean comprado) {
		this.id = id;
		this.listaCom = listaCom;
		this.producto = producto;
		this.cantidad = cantidad;
		this.comprado = comprado;
	}

	@EmbeddedId
	public ListaCompProductoId getId() {
		return id;
	}

	public void setId(ListaCompProductoId id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE })
	@JoinColumn(name = "producto_id", insertable = false, updatable = false)
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@ManyToOne
	@JoinColumn(name = "lista_comp_id", insertable = false, updatable = false)
	public ListaCom getListaCom() {
		return listaCom;
	}

	public void setListaCom(ListaCom listaCom) {
		this.listaCom = listaCom;
	}

	@Pattern(regexp = "^[1-9]\\d{0,1}$", message = "solo puede tener números positivos y enteros de dos cifras")
	@Column(name = "cantidad", nullable = false)
	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public Boolean getComprado() {
		return comprado;
	}

	public void setComprado(Boolean comprado) {
		this.comprado = comprado;
	}

	@Override
	public String toString() {
		return "ListaCompProducto [id=" + id + ", listaCom=" + listaCom + ", producto=" + producto + ", cantidad="
				+ cantidad + "]";
	}
}
