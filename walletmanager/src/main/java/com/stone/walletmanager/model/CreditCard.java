package com.stone.walletmanager.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.stone.walletmanager.converter.CryptoConverter;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@Data
@Entity
public class CreditCard {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Convert(converter = CryptoConverter.class)
    @NotEmpty
    @Pattern(regexp = "[\\s]*[0-9]+")
    @Column( unique=true )
    private String cardNumber;
    @Convert(converter = CryptoConverter.class)
    @NotEmpty
    @Pattern(regexp = "[\\s]*[0-9]+")
    private String cvv;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dueDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expirationDate;

    @Digits(integer = 10, fraction = 2)
    private Double cardLimit;

    @Digits(integer = 10, fraction = 2)
    private Double cardAmount;

    public CreditCard(){}

    public CreditCard(String cardNumber, String cvv, LocalDate expirationDate, Double cardLimit) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
        this.cardLimit = cardLimit;
    }


}
