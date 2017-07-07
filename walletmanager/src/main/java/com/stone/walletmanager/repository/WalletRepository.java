package com.stone.walletmanager.repository;

import com.stone.walletmanager.model.CreditCard;
import com.stone.walletmanager.model.Wallet;
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
public interface WalletRepository extends CrudRepository<Wallet, Long> {

    @Modifying
    @Query("update Wallet w set w.limit = :limit where w.id = :id")
    @Transactional
    void updateLimit(@Param("id") Long id, @Param("limit") Double limit);

}
