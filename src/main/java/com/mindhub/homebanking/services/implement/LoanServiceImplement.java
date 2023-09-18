package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
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

    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Override
    public void addClientLoan(Authentication authentication, LoanApplicationDTO loanApplicationDTO) {
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
