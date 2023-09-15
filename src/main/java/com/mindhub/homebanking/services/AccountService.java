package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Set;

public interface AccountService {

    List<AccountDTO> getAccountsDTO(Authentication authentication);

    AccountDTO getAccountDTO(Authentication authentication, Long id);

    Account createAccount();

    Set<Account> getCurrentAccounts(Authentication authentication);

    ResponseEntity<Object> addAccount (Authentication authentication);

    void saveAccount(Account account);
}