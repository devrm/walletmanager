package com.stone.walletmanager.service;

import com.stone.walletmanager.exception.CardAlreadyExistsException;
import com.stone.walletmanager.exception.CardNotFoundException;
import com.stone.walletmanager.exception.UserNotFoundException;
import com.stone.walletmanager.model.CreditCard;
import com.stone.walletmanager.model.NoCreditCard;
import com.stone.walletmanager.model.User;
import com.stone.walletmanager.repository.CreditCardRepository;
import com.stone.walletmanager.repository.UserRepository;
import com.stone.walletmanager.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@Service
public class CreditCardService {

    private CreditCardRepository repository;
    private UserRepository userRepository;
    private WalletRepository walletRepository;

    @Autowired
    public CreditCardService(CreditCardRepository repository,
                             UserRepository userRepository,
                             WalletRepository walletRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public List<CreditCard> getUserCards(String email) {
        List<CreditCard> userCards = this.repository.getUserCards(email);

        if (userCards.isEmpty()) {
            userCards = new ArrayList<CreditCard>(1);
            userCards.add(new NoCreditCard("No cards for payment"));
        }
        return userCards;
    }

    public void executeCreditCardPayment(Double amount, String cardNumber) throws CardNotFoundException {

        CreditCard card = this.repository.getCard(cardNumber);
        if (card != null) {
            if (isAmountToPayValid(amount, card)) {
                throw new IllegalArgumentException("Invalid amount:  "+amount + " Card limit: "+card.getCardLimit() + " Card amount: "+card.getCardAmount());
            }

            if (card.getCardAmount() != 0) {
                amount = card.getCardAmount() - amount;
                repository.modifyCard(amount, card.getCardNumber());
            }
        } else {
            throw new CardNotFoundException("Card not found for payment.");
        }
    }

    private boolean isAmountToPayValid(Double amount, CreditCard card) {
        return amount > card.getCardLimit() || amount < 0 || card.getCardAmount() < amount;
    }

    public void modifyCard(Double amount, String cardNumber) {
        repository.modifyCard(amount, cardNumber);
    }

    public void inserCard(String userEmail, CreditCard card) throws CardAlreadyExistsException, UserNotFoundException {

        final User user = this.userRepository.findByEmail(userEmail);
        if (user != null) {
            if (this.repository.getCard(card.getCardNumber()) == null) {
                user.getWallet().getCards().add(card);
                this.walletRepository.save(user.getWallet());
            } else {
                throw new CardAlreadyExistsException("Card already exists.");
            }
        } else {
            throw new UserNotFoundException("No user found with email "+userEmail);
        }
    }


}