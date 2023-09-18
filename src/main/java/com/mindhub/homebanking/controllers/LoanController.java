package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<Object> addClientLoan (Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        if (authentication == null) {
            return new ResponseEntity<>("You must be logged in", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getToAccountNumber() == null || loanApplicationDTO.getToAccountNumber().isBlank() || !accountRepository.existsByNumber(loanApplicationDTO.getToAccountNumber())) {
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);
        }
        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        if (!account.getClient().getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>("Destination account must be yours", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getLoanId() == null || !loanRepository.existsById(loanApplicationDTO.getLoanId())) {
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        if (loanApplicationDTO.getPayments() == null || !loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("This amount of payments is not allowed for this loan", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() == null || loanApplicationDTO.getAmount() <= 0 || loan.getMaxAmount() > loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("Amount must be between 1 and " + loan.getMaxAmount(), HttpStatus.FORBIDDEN);
        }
        loanService.addClientLoan(authentication,loanApplicationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}