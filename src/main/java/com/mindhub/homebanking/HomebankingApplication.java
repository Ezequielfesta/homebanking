package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

// import java.util.ArrayList;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository, LoanRepository loanRepository, TransactionRepository transactionRepository) {
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
			Account account3 = new Account();
			account3.setNumber("VIN003");
			account3.setCreationDate();
			account3.setBalance(4000.0d);
			client2.addAccount(account3);
			accountRepository.save(account3);

			Transaction transaction1 = new Transaction();
			transaction1.setTypeDebit();
			transaction1.setDate();
			transaction1.setAmount(-1500d);
			transaction1.setDescription("Grocery");
			transaction1.setAccount(account1);
			transactionRepository.save(transaction1);
			Transaction transaction2 = new Transaction();
			transaction2.setTypeCredit();
			transaction2.setDate();
			transaction2.setAmount(1200d);
			transaction2.setDescription("School");
			transaction2.setAccount(account1);
			transactionRepository.save(transaction2);
			Transaction transaction3 = new Transaction();
			transaction3.setTypeDebit();
			transaction3.setDate();
			transaction3.setAmount(-2000d);
			transaction3.setDescription("TV Purchase");
			transaction3.setAccount(account2);
			transactionRepository.save(transaction3);

			Loan loan1 = new Loan("Mortgage",500000d,List.of(12,24,36,48,60));
			//loan1.setName("Mortgage");
			//loan1.setMaxAmount(500000d);
			//loan1.setPayments(List.of(12,24,36,48,60));
			loanRepository.save(loan1);
			Loan loan2 = new Loan("Personal",100000d,List.of(6,12,24));
			loanRepository.save(loan2);
			Loan loan3 = new Loan("Automobile",300000d,List.of(6,12,24,36));
			loanRepository.save(loan3);
		};
	}
}