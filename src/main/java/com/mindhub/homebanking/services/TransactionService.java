package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface TransactionService {

    List<TransactionDTO> getTransaction(Authentication authentication);

    ResponseEntity<Object> transfer(Authentication authentication, String fromAccountNumber, String toAccountNumber, Double amount, String description);

    ResponseEntity<Object> checkTransaction(Authentication authentication, String fromAccountNumber, String toAccountNumber, Double amount, String description);

    void createTransaction(String fromAccountNumber, String toAccountNumber, Double amount, String description);
}

