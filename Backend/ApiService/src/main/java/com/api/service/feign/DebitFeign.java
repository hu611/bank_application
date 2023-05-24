package com.api.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "OpenAccountService")
@RequestMapping("/storage")
public class DebitFeign {
    @RequestMapping("/")
}
