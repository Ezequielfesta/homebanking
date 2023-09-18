package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getLoans();

    void addClientLoan (Authentication authentication, LoanApplicationDTO loanApplicationDTO);
}

