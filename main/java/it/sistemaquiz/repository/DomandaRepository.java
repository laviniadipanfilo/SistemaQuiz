package it.sistemaquiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.sistemaquiz.model.Domanda;

@Repository
public interface DomandaRepository extends JpaRepository<Domanda, Integer> {

	<Optional>Domanda findById(Long idDomanda);
}
