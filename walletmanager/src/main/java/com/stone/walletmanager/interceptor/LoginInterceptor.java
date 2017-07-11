package com.stone.walletmanager.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {


    private String walletWebTokenUrl;


    public LoginInterceptor(String walletWebTokenUrl) {
        this.walletWebTokenUrl = walletWebTokenUrl;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        String token = request.getHeader("token");

        if(StringUtils.isEmpty(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Authentication failed. Please provide needed access token.");
            return false;
        }


        RestTemplate restTemplate = new RestTemplate();
        final UriComponentsBuilder tokenPost = UriComponentsBuilder.fromHttpUrl(walletWebTokenUrl+"/token/");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("token", token);
        HttpEntity<MultiValueMap<String, String>> requestPost = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        try {
            ResponseEntity<Boolean> resp = restTemplate.postForEntity(tokenPost.toUriString(), requestPost, Boolean.class);
        } catch (HttpClientErrorException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Authentication failed. Invalid token.");

            return false;
        }

        return true;
    }


}
