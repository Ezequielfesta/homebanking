package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// import java.util.ArrayList;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository) {
		return args -> {
			Client client = new Client("Melba","Morel","melba@mindhub.com");
			clientRepository.save(client);
			Client client2 = new Client("Albert","Eins","alber@mindhub.com");
			clientRepository.save(client2);
//          ArrayList<Integer> numeros = new ArrayList<>();
//			numeros.add(32);
//			int[] numeros2 = new int[5];
		};
	}
}