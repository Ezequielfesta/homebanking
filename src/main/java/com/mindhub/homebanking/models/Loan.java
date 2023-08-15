package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private Double maxAmount;

    @ElementCollection
    private List<Integer> payments = new ArrayList<>();
    //@OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    //private Set<Account> accounts = new HashSet<>();


    public Loan(){}
    public Loan(String name, Double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public Long getId(){
        return id;
    }
    public String getName() {
        return name;
    }
    public Double getMaxAmount() { return maxAmount; }
    public List<Integer> getPayments() {
        return payments;
    }
    //public Set<Account> getAccounts() {return accounts;}

    public void setName(String name) {
        if (!name.isBlank()) {
            this.name = name;
        }
    }
    public void setMaxAmount(Double maxAmount) {
            this.maxAmount = maxAmount;
    }
    public void setPayments(List<Integer> payments) {
            this.payments = payments;
    }
    //public void addAccount(Account account){
    //    account.setClient(this);
    //    this.accounts.add(account);
    //}

    public String toString() {
        return "Loan{" + '\'' +
                "id=" + id + '\'' +
                ",name='" + name + '\'' +
                ", maxAmount='" + maxAmount + '\'' +
                ", payments='" + payments + '\'' +
                '}';
    }
}