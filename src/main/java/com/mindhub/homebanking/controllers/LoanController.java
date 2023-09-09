package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.models.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        List<Loan> listLoan = loanRepository.findAll();
        return listLoan.stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @RequestMapping(path="/loans",method= RequestMethod.POST)
    public ResponseEntity<Object> addClientLoan (Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(),loanApplicationDTO.getPayments(),client,loan);
        clientLoanRepository.save(clientLoan);
        return new ResponseEntity<>("Loan successfully created", HttpStatus.CREATED);
    }
}