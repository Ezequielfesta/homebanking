package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransaction() {
        List<Transaction> listTransaction = transactionRepository.findAll();
        return listTransaction.stream().map( transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }

    @RequestMapping(path="/transactions",method=RequestMethod.POST)
    @Transactional
    public ResponseEntity<Object> transfer (Authentication authentication, @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam Double amount, @RequestParam String description)  {
        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || amount==null || description.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (amount<=0) {
            return new ResponseEntity<>("Amount must be greater than zero", HttpStatus.FORBIDDEN);
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

        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amount, fromAccountNumber + " " + description, LocalDateTime.now());
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, toAccountNumber + " " + description, LocalDateTime.now());
        transactionDebit.setAccount(accountFrom);
        accountFrom.setBalance(accountFrom.getBalance()-transactionDebit.getAmount());
        transactionCredit.setAccount(accountTo);
        accountTo.setBalance(accountTo.getBalance()+transactionCredit.getAmount());

        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        return new ResponseEntity<>("Transfer successful", HttpStatus.CREATED);
    }
}