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
    private String email;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    private Set<Account> accounts = new HashSet<>();


    public Client(){}
    public Client(String firstName,String lastName,String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
    public Set<Account> getAccounts() {return accounts;}

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
        if (!lastName.isBlank()) {
            this.email = email;
        }
    }
    public void addAccount(Account account){
        account.setClient(this);
        this.accounts.add(account);
    }

    public String toString() {
        return "Client{" + '\'' +
                "id=" + id + '\'' +
                ",firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}