package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Rightalign;
import com.mycompany.myapp.repository.RightalignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Rightalign}.
 */
@Service
@Transactional
public class RightalignService {

    private final Logger log = LoggerFactory.getLogger(RightalignService.class);

    private final RightalignRepository rightalignRepository;

    public RightalignService(RightalignRepository rightalignRepository) {
        this.rightalignRepository = rightalignRepository;
    }

    /**
     * Save a rightalign.
     *
     * @param rightalign the entity to save.
     * @return the persisted entity.
     */
    public Rightalign save(Rightalign rightalign) {
        log.debug("Request to save Rightalign : {}", rightalign);
        return rightalignRepository.save(rightalign);
    }

    /**
     * Get all the rightaligns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Rightalign> findAll(Pageable pageable) {
        log.debug("Request to get all Rightaligns");
        return rightalignRepository.findAll(pageable);
    }


    /**
     * Get one rightalign by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rightalign> findOne(Long id) {
        log.debug("Request to get Rightalign : {}", id);
        return rightalignRepository.findById(id);
    }

    /**
     * Delete the rightalign by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rightalign : {}", id);
        rightalignRepository.deleteById(id);
    }
}
