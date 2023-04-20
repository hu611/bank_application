package com.credit.Controller;

import com.base.RestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CreditController {
    @GetMapping("/getCreditScore")
    @ResponseBody
    public RestResponse getCreditScore() {
        return null;
    }
}
