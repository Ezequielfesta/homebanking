package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    private Set<Card> cards = new HashSet<>();

    public Client(){}
    public Client(String firstName,String lastName,String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Long getId(){
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {return password;}
    public Set<Account> getAccounts() {return accounts;}
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    public Set<Card> getCards() {return cards;}

    public void setFirstName(String firstName) {
        if (!firstName.isBlank()) {
            this.firstName = firstName;
        }
    }
    public void setLastName(String lastName) {
        if (!lastName.isBlank()) {
            this.lastName = lastName;
        }
    }
    public void setEmail(String email) {
        if (!email.isBlank()) {
            this.email = email;
        }
    }
    public void setPassword(String password) {
        if (!password.isBlank()) {
            this.password = password;
        }
    }
    public void addAccount(Account account) {
        account.setClient(this);
        this.accounts.add(account);
    }
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card card){
        card.setClient(this);
        this.cards.add(card);
    }
}