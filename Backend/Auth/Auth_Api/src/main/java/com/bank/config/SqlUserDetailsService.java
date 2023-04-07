package com.bank.config;

import com.bank.mapper.BankUserMapper;
import com.bank.pojo.BankUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SqlUserDetailsService implements UserDetailsService {
    @Autowired
    BankUserMapper bankUserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username",username);

        BankUser bankUser = bankUserMapper.selectByUserName(username);
        if(bankUser == null) {
            System.out.println("user name not found");
            throw new UsernameNotFoundException("User name not found");
        }
        System.out.println(bankUser);
        return _bankUserToUserDetails(bankUser);
    }

    public UserDetails _bankUserToUserDetails(BankUser bankUser) {
        return User.withUsername(bankUser.getUsername())
                .password(bankUser.getPassword()).authorities("p1").build();
    }
}




