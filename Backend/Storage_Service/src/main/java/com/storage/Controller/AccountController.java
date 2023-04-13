package com.storage.Controller;

import com.base.RestResponse;
import com.storage.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
            if(char_cardType == '0') {
                //debit card
                accountService.openDebitAccount(prcId, username, cardType.charAt(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail("Open account failed");
        }
        return RestResponse.success();

    }

    @RequestMapping("/confirm")
    @ResponseBody
    public RestResponse open_bank_confirm(@RequestParam("username") String username,
                                                  @RequestParam("confirmcode") String confirmcode) {
        String[]userInfo = get_token_user();
        if(!userInfo[0].equals(username)) {
            return RestResponse.validfail("confirm user is different from login user");
        }
        return null;
    }
}
