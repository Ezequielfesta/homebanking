package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface ClientService {

    List<ClientDTO> getClientsDTO(Authentication authentication);

    ClientDTO getClientDTO(Authentication authentication);

    ClientDTO getClientDTOById(Authentication authentication, Long id);

    ResponseEntity<Object> register(String firstName,
                                    String lastName,
                                    String email,
                                    String password);

    ResponseEntity<Object> checkEmailExists(String email);

    ResponseEntity<Object> checkParameters(String firstName, String lastName, String email, String password);

    void saveClient(Client client);

    Client findByEmail(Authentication authentication);

}