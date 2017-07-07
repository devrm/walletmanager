package com.stone.walletpurchase.pojo.wrapper;

import lombok.Data;

/**
 * Created by rodrigo.mafra on 30/06/2017.
 */
@Data
public class PurchaseRequest {
    private String email;
    private Double amount;
}
