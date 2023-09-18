package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccountsDTO(Authentication authentication) {
        return accountService.getAccountsDTO(authentication);
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountDTO(Authentication authentication, @PathVariable Long id){
        return accountService.getAccountDTO(authentication, id);
    }

    @GetMapping("/clients/current/accounts")
    public Set<Account> getCurrentAccounts(Authentication authentication) {
        return accountService.getCurrentAccounts(authentication);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> addAccount (Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("You must be logged in", HttpStatus.FORBIDDEN);
        }
        Set<Account> setAccount = accountRepository.findByClient(clientRepository.findByEmail(authentication.getName()));
        if (setAccount.size() == 3) {
            return new ResponseEntity<>("Up to 3 accounts per client allowed", HttpStatus.FORBIDDEN);
        }
        accountService.addAccount(authentication);
        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }
}