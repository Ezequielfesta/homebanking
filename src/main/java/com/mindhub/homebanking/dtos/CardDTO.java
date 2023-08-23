package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;

import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cardHolder;
    private Card.CardType type;
    private Card.CardColor color;
    private String number;
    private String cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    public Long getId() {
        return id;
    }
    public String getCardHolder() { return cardHolder; }
    public Card.CardType getType() { return type; }
    public Card.CardColor getColor() { return color; }
    public String getNumber() {
        return number;
    }
    public String getCvv() { return cvv; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getThruDate() { return thruDate; }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
    public void setType(Card.CardType type) {
        this.type = type;
    }
    public void setColor(Card.CardColor color) {
        this.color = color;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

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
}
