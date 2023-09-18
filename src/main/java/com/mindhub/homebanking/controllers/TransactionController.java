package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransaction(Authentication authentication) {
        return transactionService.getTransaction(authentication);
    }

    @PostMapping("/transactions")
    @Transactional
    public ResponseEntity<Object> transfer (Authentication authentication, @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam Double amount, @RequestParam String description)  {
        if (authentication == null) {
            return new ResponseEntity<>("You must be logged in", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.isEmpty() || fromAccountNumber.isBlank()) {
            return new ResponseEntity<>("Origin account cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (toAccountNumber.isEmpty() || toAccountNumber.isBlank()) {
            return new ResponseEntity<>("Destination account cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (amount == null || amount <= 0) {
            return new ResponseEntity<>("Amount must be greater than zero", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty() || description.isBlank()) {
            return new ResponseEntity<>("Description cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(fromAccountNumber)) {
            return new ResponseEntity<>("Origin account doesn't exist", HttpStatus.FORBIDDEN);
        }
        Account accountFrom = accountRepository.findByNumber(fromAccountNumber);
        if (!accountFrom.getClient().getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>("Origin account must be yours", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(toAccountNumber)) {
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Origin and destination accounts must be different", HttpStatus.FORBIDDEN);
        }
        if (accountFrom.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        transactionService.createTransaction(fromAccountNumber, toAccountNumber, amount, description);
        return new ResponseEntity<>("Transfer successful", HttpStatus.CREATED);
    }
}