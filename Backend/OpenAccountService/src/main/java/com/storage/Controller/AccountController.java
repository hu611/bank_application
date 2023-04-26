package com.storage.Controller;

import com.base.RestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.storage.Dto.ConfirmMsgDto;
import com.storage.Dto.TransactionDto;
import com.storage.service.AccountService;
import com.storage.service.DecryptService;
import com.storage.service.utils.UsefulUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;


@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    DecryptService decryptService;

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

    @RequestMapping("/open")
    @ResponseBody
    public RestResponse openAccount(@RequestParam("cardType") String cardType) {
        String[] userInfo = get_token_user();
        String username = userInfo[0];
        String prcId = userInfo[1];
        char char_cardType = cardType.charAt(0);
        try {
            accountService.openAccount(prcId, username, char_cardType);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail("Open account failed");
        }
        return RestResponse.success();

    }

    @RequestMapping("/confirm")
    @ResponseBody
    public RestResponse open_bank_confirm(@RequestBody ConfirmMsgDto confirmMsgDto) throws Exception {
        String[]userInfo = get_token_user();
        JsonNode jsonNode = decryptService.aes_decrypt(confirmMsgDto.getConfirm_msg());
        String username = UsefulUtils.remove_first_and_last_char(jsonNode.get("username").toString());
        String confirmCode = UsefulUtils.remove_first_and_last_char(jsonNode.get("confirmCode").toString());
        String pinNum = UsefulUtils.remove_first_and_last_char(jsonNode.get("pinNum").toString());

        if(!userInfo[0].equals(username)) {
            return RestResponse.validfail("confirm user is different from login user");
        }

        accountService.openAccountAfterConfirm(userInfo[1],username,confirmCode,pinNum);
        return RestResponse.success();
    }
}
