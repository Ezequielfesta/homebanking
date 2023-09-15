package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    GeneralService generalService;

    @Override
    public List<AccountDTO> getAccountsDTO(Authentication authentication) {
        generalService.checkLoggedIn(authentication);
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountDTO(Authentication authentication, Long id) {
        generalService.checkLoggedIn(authentication);
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @Override
    public Set<Account> getCurrentAccounts(Authentication authentication) {
        generalService.checkLoggedIn(authentication);
        return accountRepository.findByClient(clientRepository.findByEmail(authentication.getName()));
    }
    @Override
    public Account createAccount() {
        Account account = new Account();
        account.setCreationDate();
        String randomNumber = account.getRandomNumber();
        while (accountRepository.existsByNumber(randomNumber)) {
        randomNumber = account.getRandomNumber();
        }
        account.setNumber(randomNumber);
        return account;
    }

    @Override
    public ResponseEntity<Object> addAccount (Authentication authentication) {
        generalService.checkLoggedIn(authentication);
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Account> setAccount = accountRepository.findByClient(client);
        if (setAccount.size() == 3) {
            return new ResponseEntity<>("Up to 3 accounts per client allowed", HttpStatus.FORBIDDEN);
        } else {
            Account account = createAccount();
            client.addAccount(account);
            saveAccount(account);
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        }
    }

    @Override
    public void saveAccount(Account account){
        accountRepository.save(account);
    }
}