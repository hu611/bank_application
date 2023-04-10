package com.storage.Controller;

import com.base.RestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.Dto.TransactionDto;
import com.storage.service.DecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transaction")
public class TransferController {
    @Autowired
    DecryptService decryptService;


    @PostMapping("/deposit")
    @ResponseBody
    public RestResponse deposit_money(@RequestBody TransactionDto transactionDto) {
        try {
            System.out.println(transactionDto.getTransaction());
            String transaction = transactionDto.getTransaction();
            JsonNode jsonNode = decryptService.aes_decrypt(transaction);
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principalObj instanceof String) {
                String user = principalObj.toString();

            } else {
                return RestResponse.validfail("Jwt Token error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail("Decyrption step failed, please provide a good thing");
        }
        return RestResponse.success("Success");
    }


}
