package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Rightalign} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.RightalignResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rightaligns?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RightalignCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sdf;

    public RightalignCriteria(){
    }

    public RightalignCriteria(RightalignCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.sdf = other.sdf == null ? null : other.sdf.copy();
    }

    @Override
    public RightalignCriteria copy() {
        return new RightalignCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSdf() {
        return sdf;
    }

    public void setSdf(StringFilter sdf) {
        this.sdf = sdf;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RightalignCriteria that = (RightalignCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sdf, that.sdf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sdf
        );
    }

    @Override
    public String toString() {
        return "RightalignCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sdf != null ? "sdf=" + sdf + ", " : "") +
            "}";
    }

}
