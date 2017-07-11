package com.stone.walletpurchase.config;

import com.stone.walletpurchase.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

    @Value("${wallet.tokenurl}")
    private String walletTokenUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getInterceptor()).excludePathPatterns("/error");
    }

    @Bean
    public LoginInterceptor getInterceptor() {
        return new LoginInterceptor(this.walletTokenUrl);
    }

}