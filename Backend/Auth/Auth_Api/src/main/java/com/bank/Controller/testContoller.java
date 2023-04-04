package com.bank.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class testContoller {
    @RequestMapping("/test1")
    @ResponseBody
    public String test1() {
        return "test1";
    }

    @RequestMapping("/login_success")
    @ResponseBody
    public String login_success() {
        return "Login successful";
    }

}
