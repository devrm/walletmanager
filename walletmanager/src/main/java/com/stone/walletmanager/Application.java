package com.stone.walletmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by rodrigo.mafra on 29/06/2017.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableEntityLinks
public class Application {

    public static void main(String ... args) {
        SpringApplication.run(Application.class);
    }

}
