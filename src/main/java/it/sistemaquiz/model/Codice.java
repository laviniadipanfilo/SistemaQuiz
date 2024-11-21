package it.sistemaquiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "codici")
@Entity
public class Codice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10000)
    private String codice;
//    private String matricola;
    private boolean risultato; // true se risultato è giusto, false se sbagliato
    @ManyToOne
    private Utente utente;

    public Codice(String codice, Utente utente, boolean risultato) {
    	this.codice = codice;
    	this.utente = utente;
    	this.risultato = risultato;
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

//	public String getMatricola() {
//		return matricola;
//	}
//
//	public void setMatricola(String matricola) {
//		this.matricola = matricola;
//	}

	public boolean isRisultato() {
		return risultato;
	}

	public void setRisultato(boolean risultato) {
		this.risultato = risultato;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

}
