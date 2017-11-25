package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Mesa.
 */
@Entity
@Table(name = "mesa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mesa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "area")
    private String area;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "estado")
    private String estado;

    @Column(name = "activo")
    private Boolean activo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Mesa nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Mesa ubicacion(String ubicacion) {
        this.ubicacion = ubicacion;
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getArea() {
        return area;
    }

    public Mesa area(String area) {
        this.area = area;
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public Mesa capacidad(Integer capacidad) {
        this.capacidad = capacidad;
        return this;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public Mesa estado(String estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Mesa activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mesa mesa = (Mesa) o;
        if (mesa.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mesa.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Mesa{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", area='" + getArea() + "'" +
            ", capacidad='" + getCapacidad() + "'" +
            ", estado='" + getEstado() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
