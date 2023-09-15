package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return accountService.addAccount(authentication);
    }
}