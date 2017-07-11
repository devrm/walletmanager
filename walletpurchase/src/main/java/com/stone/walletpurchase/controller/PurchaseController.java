package com.stone.walletpurchase.controller;

import com.stone.walletpurchase.exception.ExpiredCards;
import com.stone.walletpurchase.exception.NoCreditAvaiableForPurchase;
import com.stone.walletpurchase.exception.NoCreditCardAvaiable;
import com.stone.walletpurchase.pojo.CreditCard;
import com.stone.walletpurchase.pojo.User;
import com.stone.walletpurchase.pojo.wrapper.PurchaseRequest;
import com.stone.walletpurchase.service.PurchaseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    public HttpEntity executePurchase(@RequestBody PurchaseRequest purchaseRequest) throws NoCreditCardAvaiable, NoCreditAvaiableForPurchase, ExpiredCards {

        LOGGER.info("Getting user information...");

        final User user = getUserForPurchase(purchaseRequest);

        final List<CreditCard> cards = user.getWallet().getCards();
        final List<CreditCard> cardsUsed = this.purchaseService.executePurchase(cards, purchaseRequest.getAmount(), user.getWallet().getLimit());

        for (CreditCard card : cardsUsed) {
            final UriComponentsBuilder cardUpdate = UriComponentsBuilder.fromHttpUrl(walletHost+"card/"+card.getCardNumber()+"/")
                        .queryParam("amount", card.getCardAmount()).queryParam("purchaseallow", "allow");

            restTemplate.put(cardUpdate.build().toUriString(), Void.class);
        }

        return new ResponseEntity<List<CreditCard>>(cardsUsed, HttpStatus.OK);
    }

    private User getUserForPurchase(@RequestBody PurchaseRequest purchaseRequest) throws NoCreditCardAvaiable {
        final UriComponentsBuilder userEmail = UriComponentsBuilder.fromHttpUrl(walletHost+"user/findByEmail/")
                .queryParam("email", purchaseRequest.getEmail()).queryParam("purchaseallow", "allow");
        try {
            return restTemplate.getForObject(userEmail.toUriString(), User.class);
        } catch (HttpClientErrorException e) {
            LOGGER.error(e.getMessage());
            throw new NoCreditCardAvaiable("Could not get data for purchase");
        }
    }

}
