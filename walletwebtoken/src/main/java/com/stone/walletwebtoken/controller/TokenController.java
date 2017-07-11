package com.stone.walletwebtoken.controller;

import com.stone.walletwebtoken.model.Credentials;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by rodmafra on 2017-07-09.
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    private static Set<String> tokens = new HashSet<String>();

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity getToken(@RequestParam String user, @RequestParam String password) {

        if (user.equals("stone") && password.equals("hardcodedpassword")) {
            Random random = new SecureRandom();
            String token = new BigInteger(130, random).toString(32);
            tokens.add(token);
            return new ResponseEntity<String>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }

    }
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity authorize(@RequestParam String token) {
        if (tokens.contains(token)) {
            return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.FORBIDDEN);
        }
    }


}
