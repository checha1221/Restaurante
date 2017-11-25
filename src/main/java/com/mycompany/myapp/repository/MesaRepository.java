package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Mesa;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Mesa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

}
