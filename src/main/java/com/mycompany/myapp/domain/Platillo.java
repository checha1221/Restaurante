package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Platillo.
 */
@Entity
@Table(name = "platillo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Platillo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "detalle")
    private String detalle;

    @Column(name = "vencimiento")
    private LocalDate vencimiento;

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

    public Platillo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public Platillo detalle(String detalle) {
        this.detalle = detalle;
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDate getVencimiento() {
        return vencimiento;
    }

    public Platillo vencimiento(LocalDate vencimiento) {
        this.vencimiento = vencimiento;
        return this;
    }

    public void setVencimiento(LocalDate vencimiento) {
        this.vencimiento = vencimiento;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Platillo activo(Boolean activo) {
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
        Platillo platillo = (Platillo) o;
        if (platillo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), platillo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Platillo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", detalle='" + getDetalle() + "'" +
            ", vencimiento='" + getVencimiento() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
