package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ClientDTO> getClientsDTO(Authentication authentication) {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientDTO(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @Override
    public ClientDTO getClientDTOById(@PathVariable Long id) {
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @Override
    public void saveClient(Client client){
        clientRepository.save(client);
    }

    @Override
    public void register(String firstName, String lastName, String email, String password) {
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = accountService.createAccount();
        client.addAccount(account);
        saveClient(client);
        accountService.saveAccount(account);
    }

    @Override
    public Client findByEmail(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }
}
