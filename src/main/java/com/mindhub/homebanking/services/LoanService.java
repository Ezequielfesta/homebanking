package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getLoans(Authentication authentication);
    ResponseEntity<Object> addClientLoan (Authentication authentication, LoanApplicationDTO loanApplicationDTO);

    ResponseEntity<Object> checkLoan (Authentication authentication, LoanApplicationDTO loanApplicationDTO);

    void createClientLoan (Authentication authentication, LoanApplicationDTO loanApplicationDTO);
}

