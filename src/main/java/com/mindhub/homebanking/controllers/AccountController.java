package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
            List<Account> listAccount = accountRepository.findAll();
            List<AccountDTO> listAccountDTO = listAccount.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
            return listAccountDTO;
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(Authentication authentication, @PathVariable Long id){
        if (authentication != null) {
            return new AccountDTO(accountRepository.findById(id).orElse(null));
        } else return null;
    }

   @GetMapping("/clients/current/accounts")
   public Set<Account> getAccounts(Authentication authentication) {
       if (authentication != null) {
           Client client = clientRepository.findByEmail(authentication.getName());
           return accountRepository.findByClient(client);
       } else return null;
    }

   @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> addAccount (Authentication authentication) {
       if (authentication == null) {
           return new ResponseEntity<>("You must be logged in", HttpStatus.FORBIDDEN);
       }
       Client client = clientRepository.findByEmail(authentication.getName());
       Set<Account> setAccount = accountRepository.findByClient(client);
       if (setAccount.size() == 3) {
           return new ResponseEntity<>("Up to 3 accounts per client allowed", HttpStatus.FORBIDDEN);
       } else {
           Account account = new Account();
           account.setCreationDate();
           String randomNumber = account.getRandomNumber();
           while(accountRepository.existsByNumber(randomNumber)){
               randomNumber = account.getRandomNumber();
           }
           account.setNumber(randomNumber);
           client.addAccount(account);
           accountRepository.save(account);
           return new ResponseEntity<>("Account created", HttpStatus.CREATED);
       }
   }
}