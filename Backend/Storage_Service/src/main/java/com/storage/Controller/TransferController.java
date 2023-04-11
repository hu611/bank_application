package com.storage.Controller;

import com.base.RestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.Dto.TransactionDto;
import com.storage.service.DecryptService;
import com.storage.storage_service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/transaction")
public class TransferController {
    @Autowired
    DecryptService decryptService;

    @Autowired
    TransactionService transactionService;

    @PostMapping("/deposit")
    @ResponseBody
    public RestResponse deposit_money(@RequestBody TransactionDto transactionDto) {
        try {
            String transaction = transactionDto.getTransaction();
            JsonNode jsonNode = decryptService.aes_decrypt(transaction);
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principalObj instanceof String) {
                //authentication= username prcID
                String authentication = principalObj.toString();
                String[] userInfo = authentication.split(" ");
                String username = userInfo[0];
                String prcId = userInfo[1];
                try {
                    BigDecimal amount = new BigDecimal(jsonNode.get("amount").toString());
                    String account = jsonNode.get("account").toString();
                    transactionService.deposit_money(amount,username,prcId, account,transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                    return RestResponse.validfail("deposit failed");
                }
            } else {
                return RestResponse.validfail("Jwt Token error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail("Decyrption step failed, please provide a good thing");
        }
        return RestResponse.success("Success");
    }

    @RequestMapping("/generateEmail")
    @ResponseBody
    public RestResponse generate_email() {
        String[] curr_user = get_token_user();
        if(curr_user == null) {
            return RestResponse.validfail("No current user");
        }
        try {
            transactionService.generate_code(curr_user[1]);
        } catch (Exception e) {
            return RestResponse.validfail("Generate Code error");
        }
        return RestResponse.success();
    }

    public String[] get_token_user() {
        Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principalObj instanceof String) {
            //authentication= username prcID
            String authentication = principalObj.toString();
            String[] userInfo = authentication.split(" ");
            return userInfo;
        }
        return null;
    }


}
