package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransaction(Authentication authentication) {
        return transactionService.getTransaction(authentication);
    }

    @PostMapping("/transactions")
    @Transactional
    public ResponseEntity<Object> transfer (Authentication authentication, @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam Double amount, @RequestParam String description)  {
        return transactionService.transfer(authentication, fromAccountNumber, toAccountNumber, amount, description);
    }
}