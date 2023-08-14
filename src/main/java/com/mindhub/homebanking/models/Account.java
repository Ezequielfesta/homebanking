package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    private Set<Transaction> transactions = new HashSet<>();

    public Account(){}

    public Account(String number, LocalDate creationDate, Double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }

    public Long getId(){
        return id;
    }
    public Client getClient() {
        return client;
    }
    public String getNumber() {
        return number;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public Double getBalance() {
        return balance;
    }
    public Set<Transaction> getTransactions() {return transactions;}

    public void setClient(Client client) {
        this.client = client;
    }
    public void setNumber(String number) {
        if (!number.isBlank()) {
            this.number = number;
        }
    }
    public LocalDate setCreationDate() {
        creationDate = LocalDate.now();
        return creationDate;
    }
    public LocalDate setCreationDateTomorrow() {
        creationDate = LocalDate.now().plusDays(1);
        return creationDate;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        this.transactions.add(transaction);
    }

    public String toString() {
        return "Account{" + '\'' +
                "id=" + id + '\'' +
                ", number='" + number + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}