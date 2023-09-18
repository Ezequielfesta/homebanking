package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<TransactionDTO> getTransaction(Authentication authentication) {
        return transactionRepository.findAll().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
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
}
