package com.stone.walletmanager.repository;

import com.stone.walletmanager.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends CrudRepository<User, Long> {

    @RequestMapping("/findByEmail")
    @ResponseBody
    User findByEmail(@Param("email") String email);


    @Modifying
    @Query("update CreditCard c set c.cardAmount = :amount where c.cardNumber = :cardNumber")
    @Transactional
    void updateWalletLimit(@Param("amount") float amount, @Param("cardNumber") String cardNumber);


    @Override
    @RestResource(exported = false)
    public User save(User user);

}
