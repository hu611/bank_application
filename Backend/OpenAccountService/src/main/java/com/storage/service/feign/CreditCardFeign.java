package com.storage.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CreditService")
@RequestMapping("/credit")
public interface CreditCardFeign {
    @RequestMapping("/haveCreditCard")
    public boolean hasCreditCard(@RequestParam("prc_id") String prc_id) throws Exception;
}
