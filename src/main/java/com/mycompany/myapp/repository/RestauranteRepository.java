package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Restaurante;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Restaurante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

}
