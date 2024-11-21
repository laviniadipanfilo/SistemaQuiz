package it.sistemaquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.sistemaquiz.entity.Utente;
import it.sistemaquiz.repository.UtenteRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/utenti")
@RestController
public class UtenteController {

	@Autowired UtenteRepository utenteRepository;
	
	@GetMapping
    public ResponseEntity<List<Utente>> getAllUtenti() {
        List<Utente> utenti = new ArrayList<>();

        utenteRepository.findAll().forEach(utenti::add);
        return ResponseEntity.ok(utenti);
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getDettagliUtenti(@PathVariable Long id) {
	    Utente utente = utenteRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Utente non trovata"));

	    Map<String, String> dettagliUtente = new HashMap<>();
	    dettagliUtente.put("nome", utente.getNome());
	    dettagliUtente.put("cogome", utente.getCognome());
	    dettagliUtente.put("matricola", utente.getMatricola());

	    return ResponseEntity.ok(utente);
	}
	
}