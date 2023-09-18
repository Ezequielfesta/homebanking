package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.lang.Object;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard (Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {
        if (authentication == null) {
            return new ResponseEntity<>("You must be logged in", HttpStatus.FORBIDDEN);
        }
        if (cardType == null || cardType.toString().isBlank()) {
            return new ResponseEntity<>("Card type cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (cardColor == null || cardColor.toString().isBlank()) {
            return new ResponseEntity<>("Card color cannot be empty", HttpStatus.FORBIDDEN);
        }
        Set<Card> setCard = cardRepository.findByCardHolder(cardService.getCardHolder(clientService.findByEmail(authentication)));
        Set<Card> setCardTypeDebit = setCard.stream().filter(card -> card.getCardType().equals(CardType.DEBIT)).collect(Collectors.toSet());
        Set<Card> setCardTypeCredit = setCard.stream().filter(card -> card.getCardType().equals(CardType.CREDIT)).collect(Collectors.toSet());
        if (((cardType == CardType.DEBIT) && (setCardTypeDebit.size() == 3)) || ((cardType == CardType.CREDIT) && (setCardTypeCredit.size() == 3))) {
            return new ResponseEntity<>("Up to 3 cards of the same type allowed", HttpStatus.FORBIDDEN);
        }
        cardService.addCard(authentication, cardType, cardColor);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}