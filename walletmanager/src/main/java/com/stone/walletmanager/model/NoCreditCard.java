package com.stone.walletmanager.model;

import lombok.Data;

/**
 * Created by rodrigo.mafra on 30/06/2017.
 */
@Data
public class NoCreditCard extends CreditCard {

    private String message;

    public NoCreditCard(String message) {
        this.message = message;
    }

}
