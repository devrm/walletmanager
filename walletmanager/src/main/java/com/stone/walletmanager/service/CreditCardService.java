package com.stone.walletmanager.service;

import com.stone.walletmanager.exception.CardAlreadyExistsException;
import com.stone.walletmanager.model.CreditCard;
import com.stone.walletmanager.model.NoCreditCard;
import com.stone.walletmanager.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@Service
public class CreditCardService {

    private CreditCardRepository repository;

    @Autowired
    public CreditCardService(CreditCardRepository repository) {
        this.repository = repository;
    }

    public List<CreditCard> getUserCards(String email) {
        List<CreditCard> userCards = this.repository.getUserCards(email);

        if (userCards.isEmpty()) {
            userCards = new ArrayList<CreditCard>(1);
            userCards.add(new NoCreditCard("Sem cartao para pagamento"));
        }
        return userCards;
    }

    public void executeCreditCardPayment(Double amount, CreditCard card) {

        if (amount > card.getCardLimit()) {
            throw new IllegalArgumentException("Amount cannot be bigger than the credit card limit");
        }


        repository.modifyCard(amount, card.getCardNumber());
    }

    public void modifyCard(Double amount, String cardNumber) {
        repository.modifyCard(amount, cardNumber);
    }

    public void inserCard(String userEmail, CreditCard card) throws CardAlreadyExistsException {
        if (this.repository.getUserCard(userEmail, card.getCardNumber()) == null) {
            this.repository.save(card);
        } else {
            throw new CardAlreadyExistsException("This user already have this card");
        }
    }


}