package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
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
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @RequestMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard (Authentication authentication, @RequestParam Card.CardType cardType, @RequestParam Card.CardColor cardColor)
    {
        Client client = clientRepository.findByEmail(authentication.getName());
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        List<Card> listCard = cardRepository.findByCardHolder(cardHolder);
        List<Card> listCardTypeDebit = listCard.stream().filter(card -> card.getCardType().equals(Card.CardType.DEBIT)).collect(Collectors.toList());
        List<Card> listCardTypeCredit = listCard.stream().filter(card -> card.getCardType().equals(Card.CardType.CREDIT)).collect(Collectors.toList());
        if (((cardType == Card.CardType.DEBIT) && (listCardTypeDebit.size() == 3)) || ((cardType == Card.CardType.CREDIT) && (listCardTypeCredit.size() == 3)))
        {
            return new ResponseEntity<>("Up to 3 cards of the same type allowed", HttpStatus.FORBIDDEN);
        } else {
            Card card = new Card(cardHolder, cardType, cardColor, "1234-5678", "123", LocalDate.now(), LocalDate.now().plusYears(5));
            client.addCard(card);
            cardRepository.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
