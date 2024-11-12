package it.sistemaquiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "domande")
@Entity
public class Domanda {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(length=1000)
	private String domanda;
	@Column(length=3000)
    private String classeTest;
    
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDomanda() {
		return domanda;
	}
	
	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}
	
	public String getClasseTest() {
		return classeTest;
	}
	
	public void setClasseTest(String classeTest) {
		this.classeTest = classeTest;
	}
    
}