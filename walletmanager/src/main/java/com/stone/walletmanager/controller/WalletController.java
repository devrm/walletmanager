package com.stone.walletmanager.controller;

import com.stone.walletmanager.exception.WalletLimitNotPermitted;
import com.stone.walletmanager.model.User;
import com.stone.walletmanager.repository.UserRepository;
import com.stone.walletmanager.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rodrigo.mafra on 04/07/2017.
 */
@RestController
public class WalletController {


    private UserRepository userRepository;
    private WalletService walletService;

    @Autowired
    public WalletController(UserRepository userRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.walletService = walletService;
    }


    @RequestMapping(value = "/updatelimit/{email}/{maxLimit}", method = RequestMethod.POST)
    public ResponseEntity<String> setMaxLimit(@PathVariable String email, @PathVariable Double maxLimit) throws WalletLimitNotPermitted {

        final User user = this.userRepository.findByEmail(email);
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        if (user != null) {
            walletService.updateMaxLimit(user.getWallet(), maxLimit);
        } else {
            response = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

}
