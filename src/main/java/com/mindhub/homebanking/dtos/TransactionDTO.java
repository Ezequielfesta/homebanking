package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionDTO {
    private Long id;
    enum TransactionType {
        CREDIT,
        DEBIT
    }
    private TransactionType type;

    private Double amount;
    private String description;
    private LocalDateTime date;
    //private Set<AccountDTO> accounts;

    public Long getId() {
        return id;
    }
    public TransactionType getType() {
        return type;
    }
    public Double getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public LocalDateTime getDate() {
        return date;
    }
    //public Set<AccountDTO> getAccounts() {return accounts;}

    public TransactionDTO(Transaction transaction) {
        id = transaction.getId();
        type = transaction.getType();
        amount = transaction.getAmount();
        description = transaction.getDescription();
        date = transaction.getDate();
       // accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }
}
