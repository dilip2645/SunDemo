package com.mycompany.myapp.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Rightalign.
 */
@Entity
@Table(name = "rightalign")
public class Rightalign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sdf")
    private String sdf;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSdf() {
        return sdf;
    }

    public Rightalign sdf(String sdf) {
        this.sdf = sdf;
        return this;
    }

    public void setSdf(String sdf) {
        this.sdf = sdf;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rightalign)) {
            return false;
        }
        return id != null && id.equals(((Rightalign) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Rightalign{" +
            "id=" + getId() +
            ", sdf='" + getSdf() + "'" +
            "}";
    }
}
