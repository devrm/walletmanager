package com.stone.walletmanager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@Entity
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    private List<CreditCard> cards;

    @Column(name = "wallet_limit")
    private Double limit;

    @Transient
    public Double getTotalLimit() {
        Double limit = 0.0;

        if (cards != null) {
            return this.cards.stream().mapToDouble(CreditCard::getCardLimit).sum();
        }

        return limit;
    }

}
