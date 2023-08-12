package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;
    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransaction() {
        List<Transaction> listTransaction = transactionRepository.findAll();
        List<TransactionDTO> listTransactionDTO = listTransaction.stream().map( transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
        return listTransactionDTO;
    }
}
