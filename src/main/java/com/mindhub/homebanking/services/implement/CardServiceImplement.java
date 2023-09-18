package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    ClientService clientService;
    @Autowired
    CardRepository cardRepository;

    @Override
    public void addCard (Authentication authentication, CardType cardType, CardColor cardColor) {
        Client client = clientService.findByEmail(authentication);
        createCard(authentication, getCardHolder(client), cardType, cardColor, null, null, LocalDate.now(), LocalDate.now().plusYears(5));
    }

    @Override
    public String getCardHolder (Client client) {
        return client.getFirstName() + " " + client.getLastName();
    }

    @Override
    public void createCard (Authentication authentication, String cardHolder, CardType cardType, CardColor cardColor, String number, String cvv, LocalDate fromDate, LocalDate thruDate) {
        Card card = new Card(cardHolder, cardType, cardColor, null, null, LocalDate.now(), LocalDate.now().plusYears(5));
        String randomNumber = card.getRandomNumber();
        while (cardRepository.existsByNumber(randomNumber)) {
            randomNumber = card.getRandomNumber();
        }
        card.setNumber(randomNumber);
        card.setCvv();
        clientService.findByEmail(authentication).addCard(card);
        saveCard(card);
    }

    public void saveCard (Card card) {
        cardRepository.save(card);
    }
}
