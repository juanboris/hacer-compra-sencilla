package com.juanboris.springboot.backend.listaCompra.models.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class ProductoUltimoPrecioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private String mediaPrecio;
    private String marca;
    private String tipo;
    private String ultimoPrecio;

    public ProductoUltimoPrecioDTO() {}

    public ProductoUltimoPrecioDTO(Long id, String nombre, String mediaPrecio, String marca, String tipo, String ultimoPrecio) {
        this.id = id;
        this.nombre = nombre;
        this.mediaPrecio = mediaPrecio;
        this.marca = marca;
        this.tipo = tipo;
        this.ultimoPrecio = ultimoPrecio;
    }

    @NotEmpty
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Pattern(regexp = "^[0-9]{1,4}((\\.|\\,)[0-9]{1,2})?$",
            message = "tiene que ser un número con entre 1 y 4 enteros y 0 a 2 decimales")
    public String getMediaPrecio() {
        return mediaPrecio;
    }

    public void setMediaPrecio(String mediaPrecio) {
        this.mediaPrecio = mediaPrecio;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Pattern(regexp = "^[0-9]{1,4}((\\.|\\,)[0-9]{1,2})?$",
            message = "tiene que ser un número con entre 1 y 4 enteros y 0 a 2 decimales")
    public String getUltimoPrecio() {
        return ultimoPrecio;
    }

    public void setUltimoPrecio(String ultimoPrecio) {
        this.ultimoPrecio = ultimoPrecio;
    }
}

