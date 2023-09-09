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
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.2,loanApplicationDTO.getPayments(),client,loan);
        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        account.setBalance(account.getBalance()+ loanApplicationDTO.getAmount());
        Transaction transaction = new Transaction(Transaction.TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan", LocalDateTime.now());
        //if (setAccount.size() == 3) {
        //    return new ResponseEntity<>("Up to 3 accounts per client allowed", HttpStatus.FORBIDDEN);
        //}
        account.addTransaction(transaction);
        transactionRepository.save(transaction);
        accountRepository.save(account);
        clientLoanRepository.save(clientLoan);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}