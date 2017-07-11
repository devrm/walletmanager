package com.stone.walletmanager.repository;

import com.stone.walletmanager.model.CreditCard;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@RepositoryRestResource(exported = false)
public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {

    @Query("select c from User u join u.wallet w join w.cards c " +
            "where u.email = :email " +
            "order by c.dueDate desc, c.cardLimit")
    List<CreditCard> getUserCards(@Param("email") String email);

    @Query("from CreditCard where cardNumber = :cardNumber")
    CreditCard getCard(@Param("cardNumber") String cardNumber);

    @Modifying
    @Query("update CreditCard c set c.cardAmount = :amount where c.cardNumber = :cardNumber")
    @Transactional
    void modifyCard(@Param("amount") Double amount, @Param("cardNumber") String cardNumber);

    @Modifying
    @Query("delete from CreditCard c where c.cardNumber = :cardNumber")
    @Transactional
    void removeCard(@Param("cardNumber") String cardNumber);


}