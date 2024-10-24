package it.sistemaquiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.sistemaquiz.entity.RisultatoTest;

@Repository
public interface RisultatoTestRepository extends JpaRepository<RisultatoTest, Long> {
	
}