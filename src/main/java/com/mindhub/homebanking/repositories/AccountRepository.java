package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Set;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    Set<Account> findByClient (Client client);
    Account findByNumber (String number);
    Boolean existsByNumber (String number);
}