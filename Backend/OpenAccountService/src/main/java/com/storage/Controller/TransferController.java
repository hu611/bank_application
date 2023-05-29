package com.storage.Controller;

import com.base.RestResponse;
import com.base.util.DecryptUtils;
import com.base.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.storage.Dto.TransactionDto;
import com.storage.service.AccountService;
import com.storage.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.base.util.JsonUtils.preprocess_jsonnode;

@Controller
@RequestMapping("/transaction")
public class TransferController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @PostMapping("/deposit")
    @ResponseBody
    public RestResponse deposit_money(@RequestBody TransactionDto transactionDto) {
        try {
            String transaction = transactionDto.getTransaction();
            JsonNode jsonNode = DecryptUtils.aes_decrypt(transaction);
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principalObj instanceof String) {
                //authentication= username prcID
                String authentication = principalObj.toString();
                String[] userInfo = authentication.split(" ");
                String username = userInfo[0];
                String prcId = userInfo[1];
                try {
                    String str_amount = jsonNode.get("amount").toString();
                    str_amount = preprocess_jsonnode(str_amount);
                    BigDecimal amount = new BigDecimal(str_amount);
                    String account = jsonNode.get("account").toString();
                    //if not substring, then account will become ""3202020202""
                    account = preprocess_jsonnode(account);
                    String confirmCode = preprocess_jsonnode(jsonNode.get("confirmCode").toString());
                    transactionService.check_code(prcId, confirmCode);
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



    @GetMapping("/generateEmail")
    @ResponseBody
    public RestResponse generate_email() {
        String[] curr_user = get_token_user();
        if(curr_user == null) {
            return RestResponse.validfail("No current user");
        }
        try {
            transactionService.generate_code(curr_user[1]);
        } catch (Exception e) {
            e.printStackTrace();
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

    @PostMapping("/confirmEmail")
    @ResponseBody
    public RestResponse confirmEmail(@RequestParam String confirmedCode) {
        String[]userInfo = get_token_user();
        try {
            transactionService.check_code(userInfo[0], confirmedCode);
        } catch (Exception e) {
            return RestResponse.validfail("check code failed");
        }
        return RestResponse.success();

    }

    @PostMapping("/apiDepositMoney")
    @ResponseBody
    public boolean apiDepositMoney(@RequestBody String aesString) {
        String[]userInfo = get_token_user();
        String username = userInfo[0];
        String prcId = userInfo[1];
        try {
            JsonNode jsonNode = DecryptUtils.aes_decrypt(aesString);
            String amount = JsonUtils.json_to_string(jsonNode, "amount");
            String cardNum = JsonUtils.json_to_string(jsonNode, "cardNum");
            BigDecimal amountBD = new BigDecimal(amount);
            transactionService.deposit_money(amountBD, username, prcId, cardNum, aesString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
