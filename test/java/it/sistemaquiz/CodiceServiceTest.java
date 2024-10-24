package it.sistemaquiz;

import it.sistemaquiz.ordinaLista.CodiceService;
import it.sistemaquiz.entity.JSONResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CodiceServiceTest {

    private CodiceService codiceService;

    @BeforeEach
    void setUp() {
        codiceService = new CodiceService();
    }

    @Test
    void testCompilaSuccess() {
        String codiceValido = "public class CodiceUtente { public static int sum(int a, int b) { return a + b; } }";
        JSONResponse response = codiceService.compila(codiceValido);

        assertEquals("Successo", response.getStatus());
        assertEquals("Compilazione avvenuta con successo.", response.getMessaggio());
    }

    @Test
    void testCompilaErrore() {
        String codiceInvalido = "public class CodiceUtente { public static int sum(int a, int b) { return a + b }";
        JSONResponse response = codiceService.compila(codiceInvalido);

        assertEquals("Errore", response.getStatus());
        assertTrue(response.getMessaggio().contains("Errore alla riga"));
    }

    @Test
    void testEseguiCodiceCompilatoSuccess() {
        String codiceValido = "public class CodiceUtente { public static int sum(int a, int b) { return a + b; } }";
        List<Map<String, Object>> testCases = List.of(
                Map.of("method", "sum", "params", List.of(1, 2), "expected", 3)
        );

        List<Map<String, String>> risultati = codiceService.eseguiCodiceCompilato(codiceValido, testCases);

        assertEquals(1, risultati.size());
        assertEquals("GIUSTO", risultati.get(0).get("risultato"), "Il risultato atteso è GIUSTO");
    }

}
