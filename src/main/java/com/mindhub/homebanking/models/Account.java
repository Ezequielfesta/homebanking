package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

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

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Account(){}
    public Account(String number, LocalDate creationDate, Double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }

    public Long getId(){
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (!number.isBlank()) {
            this.number = number;
        }
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate setCreationDate() {
        creationDate = LocalDate.now();
        return creationDate;
    }

    public LocalDate setCreationDateTomorrow() {
        creationDate = LocalDate.now().plusDays(1);
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
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