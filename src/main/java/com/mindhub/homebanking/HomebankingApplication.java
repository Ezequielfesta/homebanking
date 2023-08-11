package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

// import java.util.ArrayList;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository) {
		return args -> {

			Client client1 = new Client("Melba","Morel","melba@mindhub.com");
			clientRepository.save(client1);
			Client client2 = new Client("Albert","Eins","alber@mindhub.com");
			clientRepository.save(client2);

			Account account1 = new Account();
			account1.setNumber("VIN001");
			account1.setCreationDate();
			account1.setBalance(5000.0d);
			client1.addAccount(account1);
			accountRepository.save(account1);
			Account account2 = new Account();
			account2.setNumber("VIN002");
			account2.setCreationDateTomorrow();
			account2.setBalance(7500.0d);
			client1.addAccount(account2);
			accountRepository.save(account2);
		};
	}
}