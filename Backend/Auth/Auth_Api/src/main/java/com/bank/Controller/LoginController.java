package com.bank.Controller;

import com.bank.mapper.BankUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    BankUserMapper bankUserMapper;

    @GetMapping("/getsalt")
    @ResponseBody
    public String getSalt(@RequestParam("username") String username) {
        if(username == null || StringUtils.isEmpty(username)) {
            throw new RuntimeException("Please enter username");
        }
        String curr_salt = bankUserMapper.selectByUserName(username).getSalt();
        System.out.println(curr_salt);
        return curr_salt;
    }

}
