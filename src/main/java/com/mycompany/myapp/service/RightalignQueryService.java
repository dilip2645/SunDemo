package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Rightalign;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.RightalignRepository;
import com.mycompany.myapp.service.dto.RightalignCriteria;

/**
 * Service for executing complex queries for {@link Rightalign} entities in the database.
 * The main input is a {@link RightalignCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Rightalign} or a {@link Page} of {@link Rightalign} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RightalignQueryService extends QueryService<Rightalign> {

    private final Logger log = LoggerFactory.getLogger(RightalignQueryService.class);

    private final RightalignRepository rightalignRepository;

    public RightalignQueryService(RightalignRepository rightalignRepository) {
        this.rightalignRepository = rightalignRepository;
    }

    /**
     * Return a {@link List} of {@link Rightalign} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Rightalign> findByCriteria(RightalignCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rightalign> specification = createSpecification(criteria);
        return rightalignRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Rightalign} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Rightalign> findByCriteria(RightalignCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rightalign> specification = createSpecification(criteria);
        return rightalignRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RightalignCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rightalign> specification = createSpecification(criteria);
        return rightalignRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Rightalign> createSpecification(RightalignCriteria criteria) {
        Specification<Rightalign> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Rightalign_.id));
            }
            if (criteria.getSdf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSdf(), Rightalign_.sdf));
            }
        }
        return specification;
    }
}
