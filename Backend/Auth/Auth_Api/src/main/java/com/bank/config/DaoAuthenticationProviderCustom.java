package com.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Mr.M
 * @version 1.0
 * @description 重写了DaoAuthenticationProvider的校验的密码的方法，因为我们统一认证入口，有一些认证方式不需要校验密码
 * @date 2023/2/24 11:40
 */
@Component
public class DaoAuthenticationProviderCustom extends DaoAuthenticationProvider {
      @Autowired
      PasswordEncoder passwordEncoder;
      @Autowired
      public void setUserDetailsService(UserDetailsService userDetailsService) {
       super.setUserDetailsService(userDetailsService);
      }

      @Override
      protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
       String presentedPassword = authentication.getCredentials().toString();
       if(!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
         throw new RuntimeException("Password does not match");
       }

      }
}
