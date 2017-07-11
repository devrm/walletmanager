package com.stone.walletmanager.service;

import com.stone.walletmanager.exception.WalletLimitNotPermitted;
import com.stone.walletmanager.model.Wallet;
import com.stone.walletmanager.repository.UserRepository;
import com.stone.walletmanager.repository.WalletRepository;
import org.springframework.stereotype.Service;

/**
 * Created by rodrigo.mafra on 04/07/2017.
 */
@Service
public class WalletService {

    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public void updateMaxLimit(final Wallet wallet, final Double maxLimit) throws WalletLimitNotPermitted {

        if (maxLimit > wallet.getTotalLimit()) {
            throw new WalletLimitNotPermitted("Max limit not allowed (bigger than the sum of creditcards limit)");
        }

        this.walletRepository.updateLimit(wallet.getId(), maxLimit);
    }


}
