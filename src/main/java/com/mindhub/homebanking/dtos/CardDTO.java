package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    public CardDTO(Card card) {
        id = card.getId();
        cardHolder = card.getClient().getFirstName() + " " + card.getClient().getLastName();
        type = card.getCardType();
        color = card.getCardColor();
        number = card.getNumber();
        cvv = card.getCvv();
        fromDate = card.getFromDate();
        thruDate = card.getThruDate();
    }

    public Long getId() {
        return id;
    }
    public String getCardHolder() { return cardHolder; }
    public CardType getType() { return type; }
    public CardColor getColor() { return color; }
    public String getNumber() {
        return number;
    }
    public String getCvv() { return cvv; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getThruDate() { return thruDate; }
}
