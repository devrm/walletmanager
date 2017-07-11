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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by rodrigo.mafra on 04/07/2017.
 */
@RestController
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

    @RequestMapping(value = "/user/findByEmail", method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity findByEmail(@RequestParam String email) {

        return new ResponseEntity<User>(this.userRepository.findByEmail(email), HttpStatus.OK);

    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public HttpEntity newUser(@RequestBody User user) {

        ResponseEntity responseEntity = null;
        user.getWallet().setLimit(user.getWallet().getTotalLimit());

        if (this.userRepository.findByEmail(user.getEmail()) == null) {
            final User save = this.userRepository.save(user);

            final Link link = this.entityLinks.linkToSingleResource(User.class, save.getId()).withSelfRel();

            responseEntity = new ResponseEntity<Link>(link, HttpStatus.OK);

        } else {
            responseEntity = new ResponseEntity<String>("User with e-mail "+user.getEmail()+" exists.", HttpStatus.CONFLICT);
        }

        return responseEntity;
    }
}
