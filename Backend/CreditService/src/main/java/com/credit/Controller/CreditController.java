package com.credit.Controller;

import com.base.RestResponse;
import com.credit.Service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CreditController {

    @Autowired
    CreditService creditService;

    @GetMapping("/getCreditScore")
    @ResponseBody
    public RestResponse getCreditScore() {
        return null;
    }

    @GetMapping("/haveCreditCard")
    @ResponseBody
    public boolean hasCreditCard(@RequestParam("prc_id") String prc_id) throws Exception {
        try {
            return creditService.hasCreditCard(prc_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Server error");
        }
    }

}
