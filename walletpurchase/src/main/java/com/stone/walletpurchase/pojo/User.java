package com.stone.walletpurchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String email;
    private Wallet wallet;
}
