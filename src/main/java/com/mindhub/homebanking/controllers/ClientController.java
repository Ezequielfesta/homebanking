package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/clients")

    public ResponseEntity<Object> register(Authentication authentication, @RequestParam String firstName, @RequestParam String lastName,
                                           @RequestParam String email, @RequestParam String password) {
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
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account();
        String randomNumber = account.getRandomNumber();
        while(accountRepository.existsByNumber(randomNumber)){
            randomNumber = account.getRandomNumber();
        }
        account.setNumber(randomNumber);
        account.setCreationDate();
        client.addAccount(account);
        clientRepository.save(client);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients")
    public List<ClientDTO> getClients(Authentication authentication) {
        if (authentication != null) {
            List<Client> listClient = clientRepository.findAll();
            List<ClientDTO> listClientDTO = listClient.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
            return listClientDTO;
        } else return null;
    }
    @PostMapping("/clients/{id}")
    public ClientDTO getClient(Authentication authentication, @PathVariable Long id) {
        if (authentication != null) {
            return new ClientDTO(clientRepository.findById(id).orElse(null));
        } else return null;
    }
    @GetMapping(path = "/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        if (authentication != null) {
            return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
        } else return null;
    }
}