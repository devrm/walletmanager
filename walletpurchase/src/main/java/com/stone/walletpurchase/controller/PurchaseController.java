package com.stone.walletpurchase.controller;

import com.stone.walletpurchase.exception.ExpiredCards;
import com.stone.walletpurchase.exception.NoCreditAvaiableForPurchase;
import com.stone.walletpurchase.exception.NoCreditCardAvaiable;
import com.stone.walletpurchase.pojo.wrapper.CardPaymentRequest;
import com.stone.walletpurchase.pojo.wrapper.PurchaseRequest;
import com.stone.walletpurchase.pojo.CreditCard;
import com.stone.walletpurchase.pojo.User;
import com.stone.walletpurchase.service.PurchaseService;
import com.sun.istack.internal.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo.mafra on 30/06/2017.
 */
@RestController
public class PurchaseController {

    private static final Logger LOGGER = Logger.getLogger(PurchaseController.class);

    private PurchaseService purchaseService;
    private RestTemplate restTemplate;
    private String walletHost;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, RestTemplate restTemplate, @Value("${walleturl}") String walletHost) {
        this.purchaseService = purchaseService;
        this.restTemplate = restTemplate;
        this.walletHost = walletHost;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ NoCreditCardAvaiable.class, NoCreditAvaiableForPurchase.class, ExpiredCards.class})
    public ResponseEntity<String> handleException(Exception e) {

        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    @ResponseBody
    public void executePurchase(@RequestBody PurchaseRequest purchaseRequest) throws NoCreditCardAvaiable, NoCreditAvaiableForPurchase, ExpiredCards {
        LOGGER.info("Getting user information...");
        final UriComponentsBuilder userEmail = UriComponentsBuilder.fromHttpUrl(walletHost+"user/search/findByEmail/")
                .queryParam("email", purchaseRequest.getEmail());
        ResponseEntity<PagedResources<User>> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(
                    userEmail.toUriString(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<PagedResources<User>>() {}, 0, 100);

        } catch (HttpClientErrorException e) {
            LOGGER.severe(e.getMessage());
            throw new NoCreditCardAvaiable("Could not get data for purchase");
        }

        List<User> users = new ArrayList<User>(responseEntity.getBody().getContent());

        final User user = users.stream().findFirst().get();

        final List<CreditCard> cards = user.getWallet().getCards();
        final List<CreditCard> cardsUsed = this.purchaseService.executePurchase(cards, purchaseRequest.getAmount(), user.getWallet().getLimit());

        for (CreditCard card : cardsUsed) {
            final UriComponentsBuilder cardUpdate = UriComponentsBuilder.fromHttpUrl(walletHost+"card/"+card.getCardNumber()+"/")
                        .queryParam("amount", card.getCardAmount());

            restTemplate.postForLocation(cardUpdate.build().toUriString(), Void.class);
        }
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> payCardAmount(@RequestBody CardPaymentRequest cardPaymentRequest) {

        ResponseEntity<String> response = new ResponseEntity<String>("Payment done.", HttpStatus.OK);



        return response;
    }


}
