package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Double amount;
    private Integer payments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Loan loan;

    //@ElementCollection
    //private Set<Integer> payments = new HashSet<>();
    //@OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    //private Set<Account> accounts = new HashSet<>();

    public ClientLoan(){}
    public ClientLoan(Double amount, Integer payments, Client client, Loan loan) {
        this.amount = amount;
        this.payments = payments;
        this.client = client;
        this.loan = loan;
    }

    public Long getId(){
        return id;
    }
    public Double getAmount() {
        return amount;
    }
    public Integer getPayments() {
        return payments;
    }
    public Client getClient() { return client; }
    public Loan getLoan() { return loan; }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public String toString() {
        return "Loan{" + '\'' +
                "id=" + id + '\'' +
                "loanId=" + loan.getId() + '\'' +
                "name=" + loan.getName() + '\'' +
                ", amount='" + amount + '\'' +
                ", payments='" + payments + '\'' +
                '}';
    }
}