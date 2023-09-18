package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO(Authentication authentication) {
        return clientService.getClientsDTO(authentication);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(Authentication authentication, @RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password) {

        if (authentication == null) {
            return new ResponseEntity<>("You must be logged in", HttpStatus.FORBIDDEN);
        }
        if (firstName.isEmpty() || firstName.isBlank()) {
            return new ResponseEntity<>("First Name cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty() || lastName.isBlank()) {
            return new ResponseEntity<>("Last Name cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty() || email.isBlank()) {
            return new ResponseEntity<>("Email cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty() || password.isBlank()) {
            return new ResponseEntity<>("Password cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        clientService.register(firstName, lastName, email, password);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id) {
        return clientService.getClientDTOById(id);
    }
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return clientService.getClientDTO(authentication);
    }
}