package it.sistemaquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import it.sistemaquiz.model.Codice;
import it.sistemaquiz.model.Domanda;
import it.sistemaquiz.repository.CodiceRepository;
import it.sistemaquiz.repository.DomandaRepository;
import it.sistemaquiz.service.CodiceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class CodiceController {

	@Autowired CodiceService codiceService;
	@Autowired CodiceRepository codiceRepository;
	@Autowired DomandaRepository domandaRepository;
	
    public CodiceController(CodiceService codiceService) {
        this.codiceService = codiceService;
    }
    
    @PostMapping("/eseguiTest")
    public ResponseEntity<?> eseguiTest(@RequestParam Long idDomanda, @RequestParam String codice) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String matricola = authentication.getName();

        Domanda domanda = this.domandaRepository.findById(idDomanda)
                .orElseThrow(() -> new IllegalArgumentException("Domanda non trovata"));
        
        String test = domanda.getClasseTest();
        
        String nomeClassePrincipale = codiceService.estraiNomeClasse(codice);
        String nomeClasseTest = codiceService.estraiNomeClasse(test);

        String codiceTestAggiornato = test.replace("CodiceUtente", nomeClassePrincipale);

        Map<String, String> codiceClassi = new HashMap<>();
        codiceClassi.put(nomeClassePrincipale, codice);
        codiceClassi.put(nomeClasseTest, codiceTestAggiornato);

        try {
            Map<String, Class<?>> classiCompilate = codiceService.caricaClassiCompilate(codiceClassi);
            Class<?> classeUtente = classiCompilate.get(nomeClassePrincipale);
            Class<?> classeTest = classiCompilate.get(nomeClasseTest);

            List<Map<String, String>> risultatiTest = codiceService.eseguiTestJUnit(classeUtente, classeTest);

            if (codiceService.getOutput()) {
//              Se tutti i test sono passati
                codiceRepository.save(new Codice(codice, matricola, true));
            } else {
//              Se ci sono errori
                codiceRepository.save(new Codice(codice, matricola, false));
            }

            if(codiceService.getOutput())
            	return ResponseEntity.ok("TEST ANDATI A BUON FINE");
            else
            	return ResponseEntity.ok(risultatiTest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
    
}