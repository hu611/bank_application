package com.api.service.feign;

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
        /*
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object detail = securityContext.getAuthentication().getDetails();
        if(detail instanceof OAuth2AuthenticationDetails) {
            return ((OAuth2AuthenticationDetails) detail).getTokenValue();
        }
         */
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYmFua19hcHBsaWNhdGlvbiJdLCJ1c2VyX25hbWUiOiJhZDI0IDMyMDIwMjAyMDIwMyIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2ODUzNTIzNDgsImF1dGhvcml0aWVzIjpbInAxIl0sImp0aSI6IjMxNGU5NzgyLThhM2YtNDNjMS05ZjRkLTU4MDRhMWJjYjJhNiIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.kgClAMtyUJAcRgx8YeFeIeqgIkt30Vwyl6a2zk6acwc";
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
