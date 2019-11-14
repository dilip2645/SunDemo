package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Rightalign;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Rightalign entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RightalignRepository extends JpaRepository<Rightalign, Long>, JpaSpecificationExecutor<Rightalign> {

}
