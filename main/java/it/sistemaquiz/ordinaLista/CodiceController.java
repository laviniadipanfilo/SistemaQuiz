package it.sistemaquiz.ordinaLista;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import it.sistemaquiz.entity.JSONResponse;
import it.sistemaquiz.entity.Utente;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
public class CodiceController {

    private final CodiceService codiceService;

    public CodiceController(CodiceService codiceService) {
        this.codiceService = codiceService;
    }

    @PostMapping("/eseguiTest")
    public ResponseEntity<List<Map<String, String>>> eseguiTest(@RequestBody Map<String, Object> input) {

    	String codice = (String) input.get("codice");
        List<Map<String, Object>> test = (List<Map<String, Object>>) input.get("test");

        JSONResponse compilazione = codiceService.compila(codice);

        if ("Errore".equals(compilazione.getStatus())) {
            return ResponseEntity.badRequest().body(List.of(Map.of("errore", compilazione.getMessaggio())));
        }

        List<Map<String, String>> outputCompilazione = codiceService.eseguiCodiceCompilato(codice, test);

        return ResponseEntity.ok(outputCompilazione);
    }
}