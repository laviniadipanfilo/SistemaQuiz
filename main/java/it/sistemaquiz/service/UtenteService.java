package it.sistemaquiz.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import it.sistemaquiz.model.Utente;
import it.sistemaquiz.repository.UtenteRepository;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public List<Utente> allUsers() {
        List<Utente> users = new ArrayList<>();

        utenteRepository.findAll().forEach(users::add);

        return users;
    }
}