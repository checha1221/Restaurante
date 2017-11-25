package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tpr;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tpr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TprRepository extends JpaRepository<Tpr, Long> {

}
