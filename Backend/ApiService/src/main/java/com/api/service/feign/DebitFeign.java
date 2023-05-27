package com.api.service.feign;

import com.api.Dto.TransactionDto;
import com.base.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "OpenAccountService")
@RequestMapping("/storage/account")
public interface DebitFeign {
    @PostMapping("/transfer")
    public RestResponse transfer(@RequestBody TransactionDto transactionDto);
}
