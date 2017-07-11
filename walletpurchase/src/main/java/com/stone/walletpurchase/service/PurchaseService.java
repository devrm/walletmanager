package com.stone.walletpurchase.service;

import com.stone.walletpurchase.exception.ExpiredCards;
import com.stone.walletpurchase.exception.NoCreditAvaiableForPurchase;
import com.stone.walletpurchase.exception.NoCreditCardAvaiable;
import com.stone.walletpurchase.pojo.CreditCard;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rodrigo.mafra on 30/06/2017.
 */
@Service
public class PurchaseService {

    public PurchaseService(){}

    final Comparator<CreditCard> dueDateComparator = (c2, c1) -> Integer.valueOf(c1.getDueDate().getDayOfMonth()).
            compareTo(Integer.valueOf(c2.getDueDate().getDayOfMonth()));
    final Comparator<CreditCard> limitComparator = (c1, c2) -> Double.valueOf(c1.getCardLimit()).
            compareTo(Double.valueOf(c2.getCardLimit()));


    public List<CreditCard> executePurchase(List<CreditCard> cards, Double amount, Double walletLimit) throws NoCreditCardAvaiable,
                                                                                                              NoCreditAvaiableForPurchase,
                                                                                                              ExpiredCards {
        checkAmounts(amount, walletLimit);

        List<CreditCard> cardsUsed = new ArrayList<CreditCard>();

        if (cards != null && (!cards.isEmpty())) {

            List<CreditCard> collectCards = cards.stream().filter(c ->
                    c.getCardAmount() < c.getCardLimit() && (c.getExpirationDate().isAfter(LocalDate.now()))
            ).collect(Collectors.toList());

            if (collectCards.isEmpty()) {
                throw new ExpiredCards("All user cards are expired.");
            }

            collectCards.sort(limitComparator);
            collectCards.sort(dueDateComparator);

            amount = consumePurchaseAmount(amount, cardsUsed, collectCards);

            if (amount > 0) {
                throw new NoCreditAvaiableForPurchase("Not enough balance to execute purchase");
            }

        } else {
            throw new NoCreditCardAvaiable("No cards for executing the purchase");
        }
        return cardsUsed;
    }

    private void checkAmounts(Double amount, Double walletLimit) {
        if (amount <= 0) {
            throw new IllegalArgumentException("The amount needs to be bigger than zero");
        } else if (amount > walletLimit) {
            throw new IllegalArgumentException("The wallet limit must be bigger than the amount. Please adjust or add more cards");
        }
    }

    private Double consumePurchaseAmount(Double amount, List<CreditCard> cardsUsed, List<CreditCard> collectCards) {
        for (CreditCard card : collectCards) {

            Double cardBalance = card.getCardLimit() - card.getCardAmount();
            if (amount > cardBalance) {
                amount = amount - cardBalance;
                card.setCardAmount(card.getCardLimit());
                cardsUsed.add(card);
            } else {
                card.setCardAmount(card.getCardAmount() + amount);
                cardsUsed.add(card);
                amount = 0.0;
                break;
            }
        }
        return amount;
    }

}
