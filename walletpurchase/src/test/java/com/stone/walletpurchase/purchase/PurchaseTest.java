package com.stone.walletpurchase.purchase;

import com.stone.walletpurchase.exception.ExpiredCards;
import com.stone.walletpurchase.exception.NoCreditAvaiableForPurchase;
import com.stone.walletpurchase.exception.NoCreditCardAvaiable;
import com.stone.walletpurchase.pojo.CreditCard;
import com.stone.walletpurchase.service.PurchaseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by rodrigo.mafra on 30/06/2017.
 */
public class PurchaseTest {

    private PurchaseService purchaseService;

    List<CreditCard> avaiableCards;
    Double walletLimit = 1000.0;

    @Before
    public void prepareForTest() {
        purchaseService = new PurchaseService();
        avaiableCards = new ArrayList<CreditCard>();

        CreditCard card1 = new CreditCard();
        card1.setCardNumber("111");
        card1.setCardLimit(1000.0);
        card1.setCardAmount(600.0);
        card1.setDueDate(LocalDate.of(2019,01,10));
        card1.setExpirationDate(LocalDate.of(2115,01,20));
        CreditCard card2 = new CreditCard();
        card2.setCardNumber("222");
        card2.setCardLimit(900.0);
        card2.setCardAmount(600.0);
        card2.setExpirationDate(LocalDate.of(2115,01,20));
        card2.setDueDate(LocalDate.of(2019,01,20));
        CreditCard card3 = new CreditCard();
        card3.setCardNumber("333");
        card3.setCardLimit(600.0);
        card3.setCardAmount(600.0);
        card3.setExpirationDate(LocalDate.of(2115,01,20));
        card3.setDueDate(LocalDate.of(2019,01,20));
        CreditCard card4 = new CreditCard();
        card4.setCardNumber("444");
        card4.setCardLimit(800.0);
        card4.setCardAmount(600.0);
        card4.setExpirationDate(LocalDate.of(2115,01,20));
        card4.setDueDate(LocalDate.of(2019,01,20));
        CreditCard card5 = new CreditCard();
        card5.setCardNumber("555");
        card5.setCardLimit(400.0);
        card5.setCardAmount(100.0);
        card5.setExpirationDate(LocalDate.of(2015,01,20));
        card5.setDueDate(LocalDate.of(2019,01,20));

        avaiableCards.add(card1);
        avaiableCards.add(card2);
        avaiableCards.add(card3);
        avaiableCards.add(card4);
        avaiableCards.add(card5);
    }


    @Test
    public void execute_purchase_with_suitable_user_card() throws NoCreditCardAvaiable, NoCreditAvaiableForPurchase, ExpiredCards {
        final List<CreditCard> suitableCards = purchaseService.executePurchase(avaiableCards, 200.0, walletLimit);

        Assert.assertTrue("444".equals(suitableCards.get(0).getCardNumber()));
    }

    @Test
    public void execute_purchase_with_suitable_user_card_floating_point_amount() throws NoCreditCardAvaiable, NoCreditAvaiableForPurchase, ExpiredCards {
        final List<CreditCard> suitableCards = purchaseService.executePurchase(avaiableCards, 120.35, walletLimit);

        Assert.assertTrue("444".equals(suitableCards.get(0).getCardNumber()));
    }


    @Test
    public void execute_purchase_with_multiple_cards() throws NoCreditCardAvaiable, NoCreditAvaiableForPurchase, ExpiredCards {

        final List<CreditCard> suitableCards = purchaseService.executePurchase(avaiableCards, 500.0, walletLimit);

        Assert.assertTrue(suitableCards.size() > 1);
    }

    @Test( expected = IllegalArgumentException.class)
    public void execute_purchase_with_invalid_amount_limit() throws NoCreditCardAvaiable, NoCreditAvaiableForPurchase, ExpiredCards {
        final List<CreditCard> suitableCards = purchaseService.executePurchase(avaiableCards, -1.0, walletLimit);
    }

    @Test( expected = IllegalArgumentException.class)
    public void execute_purchase_with_amount_beyond_limit() throws NoCreditCardAvaiable, NoCreditAvaiableForPurchase, ExpiredCards {
        final List<CreditCard> suitableCards = purchaseService.executePurchase(avaiableCards, 15000.0, walletLimit);
    }


    @Test( expected = ExpiredCards.class)
    public void execute_purchase_with_expired_cards() throws NoCreditCardAvaiable, NoCreditAvaiableForPurchase, ExpiredCards {

        avaiableCards = new ArrayList<CreditCard>();
        CreditCard card1 = new CreditCard();
        card1.setCardNumber("111");
        card1.setCardLimit(1000.0);
        card1.setCardAmount(600.0);
        card1.setDueDate(LocalDate.of(2019,01,10));
        card1.setExpirationDate(LocalDate.of(2005,01,20));
        avaiableCards.add(card1);

        final List<CreditCard> suitableCards = purchaseService.executePurchase(avaiableCards, 500.0, walletLimit);
    }

}
