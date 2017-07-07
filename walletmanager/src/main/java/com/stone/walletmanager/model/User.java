package com.stone.walletmanager.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@Entity
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String email;

    @OneToOne ( cascade = CascadeType.ALL)
    private Wallet wallet;

}
