package it.sistemaquiz.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Table(name = "codici")
@Entity
public class Codice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10000)
    private String codice;

    private String risultatoCompilazione;

    private String erroreCompilazione;

    private String risultatoFinale;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RisultatoTest> risultati;
    
    private Long timestamp;

    public Codice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getRisultatoCompilazione() {
        return risultatoCompilazione;
    }

    public void setRisultatoCompilazione(String risultatoCompilazione) {
        this.risultatoCompilazione = risultatoCompilazione;
    }

    public String getErroreCompilazione() {
        return erroreCompilazione;
    }

    public void setErroreCompilazione(String erroreCompilazione) {
        this.erroreCompilazione = erroreCompilazione;
    }

    public String getRisultatoFinale() {
        return risultatoFinale;
    }

    public void setRisultatoFinale(String risultatoFinale) {
        this.risultatoFinale = risultatoFinale;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<RisultatoTest> getRisultatiTest() {
        return risultati;
    }

    public void setRisultatiTest(List<RisultatoTest> risultati) {
        this.risultati = risultati;
    }
}
