package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CardUtilTest {

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberIsString(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber.getClass(),typeCompatibleWith(String.class));
    }

    @Test
    public void cardCvvIsCreated(){
        String cvv = CardUtils.getCardCvv();
        assertThat(cvv,is(not(emptyOrNullString())));
    }

    @Test
    public void cardCvvIsString(){
        String cvv = CardUtils.getCardCvv();
        assertThat(cvv.getClass(),typeCompatibleWith(String.class));
    }
}
