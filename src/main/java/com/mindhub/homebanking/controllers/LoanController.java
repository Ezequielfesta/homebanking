package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(Authentication authentication) {
        return loanService.getLoans(authentication);
    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<Object> addClientLoan (Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        return loanService.addClientLoan(authentication,loanApplicationDTO);
    }
}