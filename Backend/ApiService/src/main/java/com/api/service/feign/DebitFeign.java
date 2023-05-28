package com.api.service.feign;

import com.api.Dto.TransactionDto;
import com.base.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "OpenAccountService", configuration = AuthConfiguration.class)
@RequestMapping("/storage")
public interface DebitFeign {
    @PostMapping("/account/transfer")
    @ResponseBody
    public RestResponse transfer(@RequestBody TransactionDto transactionDto);
}
