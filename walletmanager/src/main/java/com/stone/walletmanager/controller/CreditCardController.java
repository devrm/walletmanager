package com.stone.walletmanager.controller;

import com.stone.walletmanager.exception.CardAlreadyExistsException;
import com.stone.walletmanager.model.CreditCard;
import com.stone.walletmanager.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@RestController
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @RequestMapping(value = "/getUserCards/{email}/", method = RequestMethod.GET)
    public List<CreditCard> getUserCards(@PathVariable String email) {
        return this.creditCardService.getUserCards(email);
    }

    @RequestMapping(value = "/card/{number}/", method = RequestMethod.POST)
    public void getCard(@PathVariable String number, @RequestParam Double amount) {
        this.creditCardService.modifyCard(amount, number);
    }

    @RequestMapping(value = "/card/{email}/", method = RequestMethod.POST)
    public void addCard(@PathVariable String email, @RequestBody CreditCard card) {


        try {
            this.creditCardService.inserCard(email, card);
        } catch (CardAlreadyExistsException e) {
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/pay/card/{number}/{amount}", method = RequestMethod.POST)
    public void payCrediCard(@PathVariable String number, @RequestParam Double amount) {
        this.creditCardService.modifyCard(amount, number);
    }

}
