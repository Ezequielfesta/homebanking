package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.utils.CardUtils;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Random;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private String cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Client client;

    public Card(){}

    public Card(String cardHolder, CardType cardType, CardColor cardColor, String number, String cvv, LocalDate fromDate, LocalDate thruDate) {
        this.cardHolder = cardHolder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    public Long getId(){
        return id;
    }
    public String getCardHolder() {
        return cardHolder;
    }
    public CardType getCardType() {
        return cardType;
    }
    public CardColor getCardColor() {
        return cardColor;
    }
    public String getRandomNumber() {
        return CardUtils.getCardNumber();
    }

    public String getNumber() {
        return number;
    }
    public String getCvv() {
        return cvv;
    }
    public LocalDate getFromDate() {
        return fromDate;
    }
    public LocalDate getThruDate() {
        return thruDate;
    }
    public Client getClient() {
        return client;
    }

    public void setCardHolder(Client client) {
        cardHolder = client.getFirstName() + " " + client.getLastName();
    }
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }
    public void setNumber(String number) {
        if (!number.isBlank()) {
            this.number = number;
        }
    }
    public void setCvv() {
        cvv = CardUtils.getCardCvv();
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}