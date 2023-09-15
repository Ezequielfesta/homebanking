package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private GeneralService generalService;

    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO(Authentication authentication) {
        return clientService.getClientsDTO(authentication);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password) {

        return clientService.register(firstName, lastName, email, password);
    }

    @PostMapping("/clients/{id}")
    public ClientDTO getClientById(Authentication authentication, @PathVariable Long id) {
        generalService.checkLoggedIn(authentication);
        return clientService.getClientDTOById(authentication, id);
    }
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        generalService.checkLoggedIn(authentication);
        return clientService.getClientDTO(authentication);
    }
}