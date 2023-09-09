package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;
import java.util.Set;

public class LoanDTO {
    private Long id;
    private String name;
    private Double maxAmount;
    private Set<Integer> payments;

    public Long getId() {
        return id;
    }
    public Double getMaxAmount() {
        return maxAmount;
    }
    public Set<Integer> getPayments() {
        return payments;
    }
    public String getName() { return name; }

    public LoanDTO(Loan loan) {
        id = loan.getId();
        name = loan.getName();
        maxAmount = loan.getMaxAmount();
        payments = loan.getPayments();
    }
}
