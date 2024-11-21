package it.sistemaquiz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.sistemaquiz.entity.Utente;
import it.sistemaquiz.service.UtenteService;

import java.util.List;

@RequestMapping("/utenti")
@RestController
public class UtenteController {
    private final UtenteService userService;

    public UtenteController(UtenteService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Utente>> allUsers() {
        List <Utente> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }
}