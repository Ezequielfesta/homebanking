package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        List<Loan> listLoan = loanRepository.findAll();
        return listLoan.stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @RequestMapping(path="/loans",method=RequestMethod.POST)
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
            return new ResponseEntity<>("Amount must be between 1 and "+loan.getMaxAmount(), HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.2,loanApplicationDTO.getPayments(),client,loan);
        account.setBalance(account.getBalance()+ loanApplicationDTO.getAmount());
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved", LocalDateTime.now());
        account.addTransaction(transaction);
        transactionRepository.save(transaction);
        accountRepository.save(account);
        clientLoanRepository.save(clientLoan);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}