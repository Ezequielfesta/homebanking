package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.GeneralService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    GeneralService generalService;

    @Override
    public List<TransactionDTO> getTransaction(Authentication authentication) {
        generalService.checkLoggedIn(authentication);
        List<Transaction> listTransaction = transactionRepository.findAll();
        return listTransaction.stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }

    public ResponseEntity<Object> checkTransaction(Authentication authentication, String fromAccountNumber, String toAccountNumber, Double amount, String description) {
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
        Account accountTo = accountRepository.findByNumber(toAccountNumber);
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Origin and destination accounts must be different", HttpStatus.FORBIDDEN);
        }
        if (accountFrom.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public void createTransaction(String fromAccountNumber, String toAccountNumber, Double amount, String description) {
        Account accountFrom = accountRepository.findByNumber(fromAccountNumber);
        Account accountTo = accountRepository.findByNumber(toAccountNumber);
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amount, fromAccountNumber + " " + description, LocalDateTime.now());
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, toAccountNumber + " " + description, LocalDateTime.now());
        transactionDebit.setAccount(accountFrom);
        accountFrom.setBalance(accountFrom.getBalance() - transactionDebit.getAmount());
        transactionCredit.setAccount(accountTo);
        accountTo.setBalance(accountTo.getBalance() + transactionCredit.getAmount());
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
    }

    public ResponseEntity<Object> transfer(Authentication authentication, @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam Double amount, @RequestParam String description) {
        checkTransaction(authentication, fromAccountNumber, toAccountNumber, amount, description);
        createTransaction(fromAccountNumber, toAccountNumber, amount, description);
        return new ResponseEntity<>("Transfer successful", HttpStatus.CREATED);
    }
}
