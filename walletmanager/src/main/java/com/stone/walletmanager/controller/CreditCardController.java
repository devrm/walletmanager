package com.stone.walletmanager.controller;

import com.stone.walletmanager.exception.CardAlreadyExistsException;
import com.stone.walletmanager.exception.CardNotFoundException;
import com.stone.walletmanager.exception.UserNotFoundException;
import com.stone.walletmanager.model.CreditCard;
import com.stone.walletmanager.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/card/{number}/", method = RequestMethod.PUT)
    public void updateAmount(@PathVariable String number, @RequestParam Double amount) {
        this.creditCardService.modifyCard(amount, number);
    }

    @RequestMapping(value = "/card/{email}/", method = RequestMethod.POST)
    public HttpEntity addCard(@PathVariable String email, @RequestBody CreditCard card) {
        ResponseEntity responseEntity = null;
        try {
            this.creditCardService.inserCard(email, card);
            responseEntity = new ResponseEntity<HttpStatus>(HttpStatus.OK);
        } catch (CardAlreadyExistsException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (UserNotFoundException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/pay/card/{number}", method = RequestMethod.POST)
    public HttpEntity payCrediCard(@PathVariable String number, @RequestParam Double amount) {
        ResponseEntity responseEntity = null;
        try {
            this.creditCardService.executeCreditCardPayment(amount, number);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        } catch (CardNotFoundException e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

}