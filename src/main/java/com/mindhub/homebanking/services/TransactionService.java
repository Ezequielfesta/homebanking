package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface TransactionService {

    List<TransactionDTO> getTransaction(Authentication authentication);

    void createTransaction(String fromAccountNumber, String toAccountNumber, Double amount, String description);
}

