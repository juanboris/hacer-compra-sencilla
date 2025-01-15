package com.juanboris.springboot.backend.listaCompra.models.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "producto_precios_historicos")
public class ProducHistoricosFecha implements Serializable {
	
	

	private static final long serialVersionUID = 7806054675371818652L;

	private Long id;
	private Producto producto;
	private BigDecimal preciosHistoricos;
	@Column
	@ColumnDefault("null")
	private Date created;

	public ProducHistoricosFecha(Long id, Producto producto, BigDecimal preciosHistoricos, Date created) {
		this.id = id;
		this.producto = producto;
		this.preciosHistoricos = preciosHistoricos;
		this.created = created;
	}
	
	public ProducHistoricosFecha(Producto producto, BigDecimal preciosHistoricos, Date created) {
		super();
		this.producto = producto;
		this.preciosHistoricos = preciosHistoricos;
		this.created = created;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productoHistoricoFechaGenerator")
	@SequenceGenerator(sequenceName = "producto_precios_historicos_sq", name = "productoHistoricoFechaGenerator", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productoId", nullable = false)
	public Producto getProducto() {
		return producto;
	}

	public ProducHistoricosFecha() {
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public BigDecimal getPreciosHistoricos() {
		return preciosHistoricos;
	}

	public void setPreciosHistoricos(BigDecimal preciosHistoricos) {
		this.preciosHistoricos = preciosHistoricos;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}