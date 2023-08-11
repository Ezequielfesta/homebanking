package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import java.time.LocalDate;

public class AccountDTO {
    private Long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;

    public Long getId() {
        return id;
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

    //private Set<Account> accounts = new HashSet<>();
    //public Set<Account> getAccounts() {return accounts;}

    public AccountDTO(Account account) {
        id = account.getId();
        number = account.getNumber();
        creationDate = account.getCreationDate();
        balance = account.getBalance();
    }
}
