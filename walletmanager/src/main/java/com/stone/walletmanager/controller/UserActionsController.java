package com.stone.walletmanager.controller;

import com.stone.walletmanager.exception.WalletLimitNotPermitted;
import com.stone.walletmanager.model.User;
import com.stone.walletmanager.repository.UserRepository;
import com.stone.walletmanager.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by rodrigo.mafra on 04/07/2017.
 */
@RestController(value = "useraction")
public class UserActionsController {

    private UserRepository userRepository;
    private WalletService walletService;
    private EntityLinks entityLinks;

    @Autowired
    public UserActionsController(UserRepository userRepository, WalletService walletService, EntityLinks entityLinks) {
        this.userRepository = userRepository;
        this.walletService = walletService;
        this.entityLinks = entityLinks;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public HttpEntity newUser(@RequestBody User user) {

        user.getWallet().setLimit(user.getWallet().getTotalLimit());

        if (this.userRepository.findByEmail(user.getEmail()) == null) {
            final User save = this.userRepository.save(user);

            final Link link = this.entityLinks.linkToSingleResource(User.class, save.getId()).withSelfRel();

            return new ResponseEntity<Link>(link, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User already exists.", HttpStatus.CONFLICT);
        }

    }

}
