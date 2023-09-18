package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<AccountDTO> getAccountsDTO(Authentication authentication) {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountDTO(Authentication authentication, Long id) {
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @Override
    public Set<Account> getCurrentAccounts(Authentication authentication) {
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
    public void addAccount (Authentication authentication) {
            Account account = createAccount();
            clientRepository.findByEmail(authentication.getName()).addAccount(account);
            saveAccount(account);
    }

    @Override
    public void saveAccount(Account account){
        accountRepository.save(account);
    }
}