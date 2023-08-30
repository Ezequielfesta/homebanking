package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

// import java.util.ArrayList;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);}

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, TransactionRepository transactionRepository, CardRepository cardRepository) {
		return args -> {

			Client client1 = new Client("Melba","Morel","melba@mindhub.com",passwordEncoder.encode("melba"));
			clientRepository.save(client1);
			Client client2 = new Client("Albert","Eins","albert@mindhub.com",passwordEncoder.encode("albert"));
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

			Loan loan1 = new Loan("Mortgage",500000d, Set.of(12,24,36,48,60));
			loanRepository.save(loan1);
			Loan loan2 = new Loan("Personal",100000d,Set.of(6,12,24));
			loanRepository.save(loan2);
			Loan loan3 = new Loan("Automobile",300000d,Set.of(6,12,24,36));
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000d,60,client1,loan1);
			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan(50000d,12,client1,loan2);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan2);
			ClientLoan clientLoan3 = new ClientLoan(100000d,24,client2,loan2);
			client2.addClientLoan(clientLoan3);
			loan2.addClientLoan(clientLoan3);
			clientLoanRepository.save(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan(200000d,36,client2,loan3);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(null,Card.CardType.DEBIT,Card.CardColor.GOLD,"1234-5678-9101","101",LocalDate.now(),LocalDate.now().plusYears(5));
			card1.setCardHolder(null);
			Card card2 = new Card(null,Card.CardType.CREDIT,Card.CardColor.TITANIUM,"1234-8765-9991","991",LocalDate.now(),LocalDate.now().plusYears(5));
			card2.setCardHolder(null);
			Card card3 = new Card(null,Card.CardType.CREDIT,Card.CardColor.SILVER,"1234-8766-8881","881",LocalDate.now(),LocalDate.now().plusYears(5));
			card3.setCardHolder(null);
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
	}
}