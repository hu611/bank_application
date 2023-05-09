package com.credit.Service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(name="OpenAccountService", configuration = AuthConfiguration.class)
@RequestMapping("/storage")
public interface DebitFeign {
    @RequestMapping("/test1")
    @ResponseBody
    public String test1();
}
