package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    GeneralService generalService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ClientDTO> getClientsDTO(Authentication authentication) {
        generalService.checkLoggedIn(authentication);
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientDTO(Authentication authentication) {
        generalService.checkLoggedIn(authentication);
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @Override
    public ClientDTO getClientDTOById(Authentication authentication, @PathVariable Long id) {
        generalService.checkLoggedIn(authentication);
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @Override
    public ResponseEntity<Object> checkEmailExists(String email) {
        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        } else return null;
    }

    @Override
    public void saveClient(Client client){
        clientRepository.save(client);
    }

    @Override
    public ResponseEntity<Object> register(String firstName, String lastName, String email, String password) {
        checkParameters(firstName, lastName, email, password);
        checkEmailExists(email);
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = accountService.createAccount();
        client.addAccount(account);
        saveClient(client);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> checkParameters(String firstName, String lastName, String email, String password) {
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
        return null;
    }
    @Override
    public Client findByEmail(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }
}
