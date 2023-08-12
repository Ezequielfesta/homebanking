package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private TransactionType type;
    enum TransactionType {
        CREDIT,
        DEBIT
    }
    private Double amount;
    private String description;
    private LocalDateTime date;

    //@OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    //private Set<Account> accounts = new HashSet<>();

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


    //public Set<Account> getAccounts() {return accounts;}

    public void setAmount(Double amount) {
        this.amount = amount;
        }
    public void setDescription(String description) {
        if (!description.isBlank()) {
            this.description = description;
        }
    }
    public void setDate(LocalDateTime date) {
        date = LocalDateTime.now();
    }

  //  public void addAccount(Account account){
  //      account.setClient(this);
  //      this.accounts.add(account);}

    public String toString() {
        return "Transaction{" + '\'' +
                "id=" + id + '\'' +
                ",type='" + type + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}