package com.storage.service.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

@Configuration
public class AuthConfiguration {
    public String get_jwt_token() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object detail = securityContext.getAuthentication().getDetails();
        if(detail instanceof OAuth2AuthenticationDetails) {
            return ((OAuth2AuthenticationDetails) detail).getTokenValue();
        }
        return null;

    }
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.header("Authorization","Bearer " + get_jwt_token());
            }
        };
    }
}
