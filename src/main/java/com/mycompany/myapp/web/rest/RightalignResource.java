package com.mycompany.myapp.web.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.mycompany.myapp.common.ReportGenerator;
import com.mycompany.myapp.common.FileOperation;
import com.mycompany.myapp.common.ReportFileType;
import org.springframework.core.io.InputStreamResource;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.myapp.domain.Rightalign;
import com.mycompany.myapp.service.RightalignService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.RightalignCriteria;
import com.mycompany.myapp.service.RightalignQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Rightalign}.
 */
@RestController
@RequestMapping("/api")
public class RightalignResource {

    private final Logger log = LoggerFactory.getLogger(RightalignResource.class);

    private static final String ENTITY_NAME = "rightalign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RightalignService rightalignService;

    private final RightalignQueryService rightalignQueryService;

    private final ReportGenerator reportGenerator;

    public RightalignResource(RightalignService rightalignService, RightalignQueryService rightalignQueryService, ReportGenerator reportGenerator) {
        this.rightalignService = rightalignService;
        this.rightalignQueryService = rightalignQueryService;
        this.reportGenerator = reportGenerator;
    }

    /**
     * {@code POST  /rightaligns} : Create a new rightalign.
     *
     * @param rightalign the rightalign to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rightalign, or with status {@code 400 (Bad Request)} if the rightalign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rightaligns")
    public ResponseEntity<Rightalign> createRightalign(@RequestBody Rightalign rightalign) throws URISyntaxException {
        log.debug("REST request to save Rightalign : {}", rightalign);
        if (rightalign.getId() != null) {
            throw new BadRequestAlertException("A new rightalign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rightalign result = rightalignService.save(rightalign);
        return ResponseEntity.created(new URI("/api/rightaligns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rightaligns} : Updates an existing rightalign.
     *
     * @param rightalign the rightalign to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rightalign,
     * or with status {@code 400 (Bad Request)} if the rightalign is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rightalign couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rightaligns")
    public ResponseEntity<Rightalign> updateRightalign(@RequestBody Rightalign rightalign) throws URISyntaxException {
        log.debug("REST request to update Rightalign : {}", rightalign);
        if (rightalign.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rightalign result = rightalignService.save(rightalign);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rightalign.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rightaligns} : get all the rightaligns.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rightaligns in body.
     */
    @GetMapping("/rightaligns")
    public ResponseEntity<List<Rightalign>> getAllRightaligns(RightalignCriteria criteria, Pageable pageable,@RequestParam(value = "exportType", required = false) String exportType) {
        log.debug("REST request to get Rightaligns by criteria: {}", criteria);
        if(exportType==null) {
        PageRequest pageRequest;
        Sort.Order order = new Sort.Order(pageable.getSort().iterator().next().getDirection(),pageable.getSort().iterator().next().getProperty()).ignoreCase();
        pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(order));
        Page<Rightalign> page = rightalignQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
        else{
           
        List<String> excludedHeader = new ArrayList<>();
			excludedHeader.add("id");
			Date date = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("hhmmssSSS");
			String strDate = dateFormat.format(date);
			String fileName = "Demo_"+strDate;

			List<Rightalign> rightalignList = rightalignQueryService.findByCriteria(criteria);

			ReportFileType  report = reportGenerator.generateListingReport(
                rightalignList,
					exportType,
					getRightalignReportHeader(),
					"Rightalign",
					"Rightalign",
					"Rightalign Report",
					excludedHeader,
					null);           
			if(report.getReportFile().exists()){
				FileOperation inputStream;
				try {
					inputStream = new FileOperation(report.getReportFile());
					return new ResponseEntity(new InputStreamResource(inputStream), report.getHttpHeader(),
							HttpStatus.OK);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
        }
        return null;
    }

    /**
    * {@code GET  /rightaligns/count} : count all the rightaligns.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/rightaligns/count")
    public ResponseEntity<Long> countRightaligns(RightalignCriteria criteria) {
        log.debug("REST request to count Rightaligns by criteria: {}", criteria);
        return ResponseEntity.ok().body(rightalignQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rightaligns/:id} : get the "id" rightalign.
     *
     * @param id the id of the rightalign to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rightalign, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rightaligns/{id}")
    public ResponseEntity<Rightalign> getRightalign(@PathVariable Long id) {
        log.debug("REST request to get Rightalign : {}", id);
        Optional<Rightalign> rightalign = rightalignService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rightalign);
    }

    /**
     * {@code DELETE  /rightaligns/:id} : delete the "id" rightalign.
     *
     * @param id the id of the rightalign to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rightaligns/{id}")
    public ResponseEntity<Void> deleteRightalign(@PathVariable Long id) {
        log.debug("REST request to delete Rightalign : {}", id);
        rightalignService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    public static Map<String, String> getRightalignReportHeader() {
        Map<String, String> reportHeaders = new LinkedHashMap<>();
            reportHeaders.put("sdf","Sdf");
        return reportHeaders;
    }
}
