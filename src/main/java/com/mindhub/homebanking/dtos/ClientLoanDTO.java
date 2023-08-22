package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
    private Long id;
    private String name;
    private Double amount;
    private Integer payments;
    private Client client;
    private Loan loan;

    public Long getId() {
        return id;
    }
    public Double getAmount() {
        return amount;
    }
    public Integer getPayments() {
        return payments;
    }
    public Client getClient() {
        return client;
    }
    public Loan getLoan() { return loan; }

    public String getName() { return name; }

    public ClientLoanDTO(ClientLoan clientLoan) {
        id = clientLoan.getId();
        amount = clientLoan.getAmount();
        payments = clientLoan.getPayments();
        client = clientLoan.getClient();
        loan = clientLoan.getLoan();
        name = loan.getName();
    }
}
