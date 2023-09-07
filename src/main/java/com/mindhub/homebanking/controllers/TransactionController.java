package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
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
        List<TransactionDTO> listTransactionDTO = listTransaction.stream().map( transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
        return listTransactionDTO;
    }


    public enum TransferType {
        own,
        third
    }
    @RequestMapping(path= "/transactions",method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Object> transfer (Authentication authentication, @RequestParam TransferType trasnferType, @RequestParam String accountFromNumber, @RequestParam String accountToNumber, @RequestParam Double amount, @RequestParam String description)  {

        Client client = clientRepository.findByEmail(authentication.getName());
        List<Account> clientAccounts = accountRepository.findByClient(client);

        if (trasnferType == TransferType.third) {
            return new ResponseEntity<>("Not implemented", HttpStatus.FORBIDDEN);
        }
        if (accountFromNumber.isEmpty() || accountToNumber.isEmpty() || amount!=null || description.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(accountFromNumber) == null) {
            return new ResponseEntity<>("Origin account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (trasnferType == TransferType.own && !accountRepository.existsByNumber(accountToNumber)) {
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);
        } //else if (trasnferType == TransferType.third){}

        if (accountFromNumber.equals(accountToNumber)) {
            return new ResponseEntity<>("Origin and destination accounts must be different", HttpStatus.FORBIDDEN);
        }
        Account accountFrom = accountRepository.findByNumber(accountFromNumber);
        if (!accountFrom.getClient().getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>("Origin account must be yours", HttpStatus.FORBIDDEN);
        }
        if (accountFrom.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        Transaction transactionDebit = new Transaction(Transaction.TransactionType.DEBIT, amount, accountFromNumber + description, LocalDateTime.now());
        Transaction transactionCredit = new Transaction(Transaction.TransactionType.CREDIT, amount, accountToNumber + description, LocalDateTime.now());
        if (trasnferType == TransferType.own && accountRepository.existsByNumber(accountToNumber)) {
            Account accountTo = accountRepository.findByNumber(accountToNumber);
            transactionCredit.setAccount(accountTo);
            accountTo.setBalance(accountFrom.getBalance()+transactionCredit.getAmount());
            accountRepository.save(accountTo);
        } //else if trasnferType == TransferType.third {}
        transactionDebit.setAccount(accountFrom);
        accountFrom.setBalance(accountFrom.getBalance()-transactionDebit.getAmount());
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);
        accountRepository.save(accountFrom);
        return new ResponseEntity<>("Transfer successful", HttpStatus.CREATED);
    }
}
