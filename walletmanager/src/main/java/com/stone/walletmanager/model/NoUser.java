package com.stone.walletmanager.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@Data
public class NoUser extends User{

    private String message;

    public NoUser(String message) {
        this.message = message;
    }


}
