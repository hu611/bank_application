package com.storage.service.feign;

import com.base.pojo.CreditCard;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CreditService", fallbackFactory = CreditCardFallBackFactory.class, configuration = AuthConfiguration.class)
@RequestMapping("/credit")
public interface CreditCardFeign {
    @RequestMapping("/haveCreditCard")
    public boolean hasCreditCard(@RequestParam("prc_id") String prc_id) throws Exception;

    @GetMapping("/getCreditCardInfo")
    public CreditCard getCreditCard(@RequestParam("prc_id") String prc_id) throws Exception;
}
