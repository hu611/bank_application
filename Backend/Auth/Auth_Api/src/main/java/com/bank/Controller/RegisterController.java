package com.bank.Controller;

import com.bank.Service.RegisterService;
import com.dto.RegisterDto;
import com.base.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    @ResponseBody
    public RestResponse register(@RequestBody RegisterDto registerDto) {
        try {
            int insert = registerService.register(registerDto);
            if (insert == 1) {
                return RestResponse.success("Registration successful");
            } else {
                return RestResponse.validfail("Registration failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail("Registration failure");
        }


    }
}
