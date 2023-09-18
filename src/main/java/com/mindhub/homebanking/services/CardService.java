package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import java.time.LocalDate;

public interface CardService {
    void addCard (Authentication authentication, CardType cardType, CardColor cardColor);

    String getCardHolder (Client client);

    void createCard (Authentication authentication, String cardHolder, CardType cardType, CardColor cardColor, String number, String cvv, LocalDate fromDate, LocalDate thruDate);

    void saveCard(Card card);

}