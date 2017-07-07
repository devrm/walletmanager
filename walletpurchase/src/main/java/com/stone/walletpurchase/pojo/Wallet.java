package com.stone.walletpurchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallet {

    private Long id;
    private List<CreditCard> cards;
    private Double limit;

}
