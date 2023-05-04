package com.credit.Controller;

import com.base.RestResponse;
import com.base.pojo.CreditCard;
import com.credit.Service.CreditService;
import com.credit.dto.CardInfoDto;
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
        return RestResponse.success("Congrats");
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

    @GetMapping("/getCreditCardInfo")
    @ResponseBody
    public CreditCard getCreditCard(@RequestParam("prc_id") String prc_id) throws Exception {
        return creditService.getCreditCardByPrcId(prc_id);
    }

}
