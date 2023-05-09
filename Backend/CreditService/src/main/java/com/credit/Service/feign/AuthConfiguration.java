package com.credit.Service.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYmFua19hcHBsaWNhdGlvbiJdLCJ1c2VyX25hbWUiOiJhZDIzIDMyMDIwMjAyMDIwMiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2ODM2MDQ1MTQsImF1dGhvcml0aWVzIjpbInAxIl0sImp0aSI6ImRmZjI0ZDBjLWY2NjUtNGM1ZC1iMjBmLTZkYzQ4NmUzMThhNCIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.imjaMnlC8Imz7naR4Wp3po9qXAMpxSf47MfXDjPN1rM");
            }
        };
    }
}
