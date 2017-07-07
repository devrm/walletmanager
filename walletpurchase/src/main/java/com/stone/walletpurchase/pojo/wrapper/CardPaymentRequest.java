package com.stone.walletpurchase.pojo.wrapper;

import lombok.Data;

/**
 * Created by rodrigo.mafra on 06/07/2017.
 */
@Data
public class CardPaymentRequest {

    private String email;
    private String cardNumber;
    private Double amountToBePaid;

}
