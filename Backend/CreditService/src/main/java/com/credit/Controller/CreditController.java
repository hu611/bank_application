package com.credit.Controller;

import com.base.RestResponse;
import com.base.pojo.CreditCard;
import com.credit.Service.CreditService;
import com.credit.dto.CardInfoDto;
import com.credit.dto.TransactionDto;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CreditController implements InitializingBean {



    @Autowired
    CreditService creditService;

    @Override
    public void afterPropertiesSet() throws Exception {
        creditService.initializeRedis();
    }

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

    @PostMapping("/creditPay")
    @ResponseBody
    public boolean creditPay(@RequestBody String aesString) throws Exception {
        return creditService.creditPay(aesString);
    }

    @PostMapping("/registerCreditCard")
    @ResponseBody
    public void registerCreditCard(@RequestBody TransactionDto transactionDto) {
        creditService.registerCreditCard(transactionDto.getTransaction());
    }

}
