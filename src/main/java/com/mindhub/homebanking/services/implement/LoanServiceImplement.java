package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.GeneralService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    GeneralService generalService;

    @Override
    public List<LoanDTO> getLoans(Authentication authentication) {
        generalService.checkLoggedIn(authentication);
        List<Loan> listLoan = loanRepository.findAll();
        return listLoan.stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Object> addClientLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        generalService.checkLoggedIn(authentication);
        checkLoan(authentication, loanApplicationDTO);
        createClientLoan(authentication, loanApplicationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> checkLoan(Authentication authentication, LoanApplicationDTO loanApplicationDTO) {
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
        return null;
    }

    @Override
    public void createClientLoan(Authentication authentication, LoanApplicationDTO loanApplicationDTO) {
        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        Client client = clientService.findByEmail(authentication);
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments(), client, loan);
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved", LocalDateTime.now());
        account.addTransaction(transaction);
        transactionRepository.save(transaction);
        accountRepository.save(account);
        clientLoanRepository.save(clientLoan);
    }
}
