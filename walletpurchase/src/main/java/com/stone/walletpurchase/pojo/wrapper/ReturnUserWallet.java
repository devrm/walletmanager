package com.stone.walletpurchase.pojo.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stone.walletpurchase.pojo.User;
import lombok.Data;

/**
 * Created by rodrigo.mafra on 01/07/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnUserWallet {

    private User user;

}
