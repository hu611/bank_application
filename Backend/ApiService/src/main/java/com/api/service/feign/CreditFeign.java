package com.api.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "CreditService", configuration = AuthConfiguration.class)
@RequestMapping("/credit")
public interface CreditFeign {
    @PostMapping("/creditPay")
    @ResponseBody
    public boolean creditPay(@RequestBody String aesString);
}
