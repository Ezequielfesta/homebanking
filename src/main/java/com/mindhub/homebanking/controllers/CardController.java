package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @RequestMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard (Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {
        Client client = clientRepository.findByEmail(authentication.getName());
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        Set<Card> setCard = cardRepository.findByCardHolder(cardHolder);
        Set<Card> setCardTypeDebit = setCard.stream().filter(card -> card.getCardType().equals(CardType.DEBIT)).collect(Collectors.toSet());
        Set<Card> setCardTypeCredit = setCard.stream().filter(card -> card.getCardType().equals(CardType.CREDIT)).collect(Collectors.toSet());
        if (((cardType == CardType.DEBIT) && (setCardTypeDebit.size() == 3)) || ((cardType == CardType.CREDIT) && (setCardTypeCredit.size() == 3))) {
            return new ResponseEntity<>("Up to 3 cards of the same type allowed", HttpStatus.FORBIDDEN);
        } else {
            Card card = new Card(cardHolder, cardType, cardColor, null, null, LocalDate.now(), LocalDate.now().plusYears(5));
            String randomNumber = card.getRandomNumber();
            while(cardRepository.existsByNumber(randomNumber)){
                randomNumber = card.getRandomNumber();
            }
            card.setNumber(randomNumber);
            card.setCvv();
            client.addCard(card);
            cardRepository.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
