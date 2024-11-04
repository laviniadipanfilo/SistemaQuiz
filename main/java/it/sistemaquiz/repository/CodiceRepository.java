package it.sistemaquiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.sistemaquiz.model.Codice;

@Repository
public interface CodiceRepository extends JpaRepository<Codice, Long> {
	
}