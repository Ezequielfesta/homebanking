package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    GeneralService generalService;
    @Autowired
    ClientService clientService;
    @Autowired
    CardRepository cardRepository;

    @Override
    public ResponseEntity<Object> addCard (Authentication authentication, CardType cardType, CardColor cardColor) {
        generalService.checkLoggedIn(authentication);
        checkCard(cardType,cardColor);
        Client client = clientService.findByEmail(authentication);
        getCardHolder(client);
        checkCardType(authentication, cardType);
        createCard(authentication, getCardHolder(client), cardType, cardColor, null, null, LocalDate.now(), LocalDate.now().plusYears(5));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> checkCard (CardType cardType, CardColor cardColor) {
        if (cardType == null || cardType.toString().isBlank()) {
            return new ResponseEntity<>("Card type cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (cardColor == null || cardColor.toString().isBlank()) {
            return new ResponseEntity<>("Card color cannot be empty", HttpStatus.FORBIDDEN);
        } return null;
    }

    @Override
    public String getCardHolder (Client client) {
        return client.getFirstName() + " " + client.getLastName();
    }
    @Override
    public ResponseEntity<Object> checkCardType (Authentication authentication, CardType cardType){
        Set<Card> setCard = cardRepository.findByCardHolder(getCardHolder(clientService.findByEmail(authentication)));
        Set<Card> setCardTypeDebit = setCard.stream().filter(card -> card.getCardType().equals(CardType.DEBIT)).collect(Collectors.toSet());
        Set<Card> setCardTypeCredit = setCard.stream().filter(card -> card.getCardType().equals(CardType.CREDIT)).collect(Collectors.toSet());
        if (((cardType == CardType.DEBIT) && (setCardTypeDebit.size() == 3)) || ((cardType == CardType.CREDIT) && (setCardTypeCredit.size() == 3))) {
            return new ResponseEntity<>("Up to 3 cards of the same type allowed", HttpStatus.FORBIDDEN);
        } return null;
    }

    @Override
    public ResponseEntity<Object> createCard (Authentication authentication, String cardHolder, CardType cardType, CardColor cardColor, String number, String cvv, LocalDate fromDate, LocalDate thruDate) {
        generalService.checkLoggedIn(authentication);
        Client client = clientService.findByEmail(authentication);
        Card card = new Card(getCardHolder(client), cardType, cardColor, null, null, LocalDate.now(), LocalDate.now().plusYears(5));
        String randomNumber = card.getRandomNumber();
        while (cardRepository.existsByNumber(randomNumber)) {
            randomNumber = card.getRandomNumber();
        }
        card.setNumber(randomNumber);
        card.setCvv();
        client.addCard(card);
        saveCard(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public void saveCard (Card card) {
        cardRepository.save(card);
    }
}
