package com.storage.Controller;

import com.base.RestResponse;
import com.base.util.DecryptUtils;
import com.base.util.FileUtils;
import com.base.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.storage.Dto.ConfirmMsgDto;
import com.storage.service.AccountService;
import com.storage.service.utils.UsefulUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


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
            accountService.openAccount(prcId, username, char_cardType);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail("Open account failed");
        }
        return RestResponse.success();

    }

    @RequestMapping("/confirmDebit")
    @ResponseBody
    public RestResponse open_bank_confirm(@RequestBody ConfirmMsgDto confirmMsgDto) throws Exception {
        String[]userInfo = get_token_user();
        JsonNode jsonNode = DecryptUtils.aes_decrypt(confirmMsgDto.getConfirm_msg());
        String username = UsefulUtils.remove_first_and_last_char(jsonNode.get("username").toString());
        String confirmCode = UsefulUtils.remove_first_and_last_char(jsonNode.get("confirmCode").toString());
        String pinNum = UsefulUtils.remove_first_and_last_char(jsonNode.get("pinNum").toString());

        if(!userInfo[0].equals(username)) {
            return RestResponse.validfail("confirm user is different from login user");
        }
        accountService.openDebitAccountAfterConfirm(userInfo[1],username,confirmCode,pinNum);
        return RestResponse.success();
    }

    @RequestMapping("/confirmCredit")
    @ResponseBody
    public RestResponse credit_confirm(@RequestPart("aes_message") String aes_message,
                                       @RequestPart("IdCard")MultipartFile IdCardFile,
                                       @RequestPart("salaryCard") MultipartFile salaryCard,
                                       @RequestPart("addressProof") MultipartFile addressProof,
                                       @RequestPart("taxDocument") MultipartFile taxDocument) throws Exception {
        String[]userInfo = get_token_user();
        JsonNode jsonNode = DecryptUtils.aes_decrypt(aes_message);
        String username = JsonUtils.preprocess_jsonnode(jsonNode.get("username").toString());
        if(!username.equals(userInfo[0])) {
            //not same record
            return RestResponse.validfail("You think you are smart? Don't fool us");
        }
        String folderPath = "Images/CreditAudit/" + userInfo[1]; // Replace with your desired folder path
        FileUtils.create_folder(folderPath);
        folderPath = folderPath + "/";
        FileUtils.save_image(folderPath,IdCardFile, salaryCard, addressProof, taxDocument);
        return RestResponse.success();
    }
}
