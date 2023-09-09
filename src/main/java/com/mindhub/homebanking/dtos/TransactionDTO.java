package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private Transaction.TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;

    public TransactionDTO(Transaction transaction) {
        id = transaction.getId();
        type = transaction.getType();
        amount = transaction.getAmount();
        description = transaction.getDescription();
        date = transaction.getDate();
    }

    public Long getId() {
        return id;
    }
    public Transaction.TransactionType getType() {
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
}