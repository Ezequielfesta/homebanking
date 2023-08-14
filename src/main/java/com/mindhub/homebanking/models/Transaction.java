package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Transaction {
    public enum TransactionType {
        CREDIT,
        DEBIT
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private TransactionType type;

    private Double amount;
    private String description;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction(){}

    public Transaction(TransactionType type, Double amount, String description, LocalDateTime date) {

        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Long getId(){
        return id;
    }
    public TransactionType getType(){
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


    public void setAccount(Account account) {
        this.account = account;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
        }
    public void setDescription(String description) {
        if (!description.isBlank()) {
            this.description = description;
        }
    }
    public void setDate() {
        date = LocalDateTime.now().minusDays(1);
    }
    public void setTypeCredit(){ type = TransactionType.CREDIT; }
    public void setTypeDebit(){ type = TransactionType.DEBIT; }

    public String toString() {
        return "Transaction{" + '\'' +
                "id=" + id + '\'' +
                ", account='" + account + '\'' +
                ", type='" + type + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}