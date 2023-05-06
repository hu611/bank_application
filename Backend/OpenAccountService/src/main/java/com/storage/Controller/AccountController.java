package com.storage.Controller;

import com.base.RestResponse;
import com.base.util.DecryptUtils;
import com.base.util.FileUtils;
import com.base.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.storage.Constants;
import com.storage.Dto.CardInfoDto;
import com.storage.Dto.ConfirmMsgDto;
import com.storage.Dto.ProduceMessageDto;
import com.storage.Dto.TransactionDto;
import com.storage.service.AccountService;
import com.storage.service.feign.KafkaFeign;
import com.storage.service.utils.UsefulUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    KafkaFeign kafkaFeign;

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
        String pin_num = JsonUtils.preprocess_jsonnode(jsonNode.get("pinNum").toString());
        String confirmCode = JsonUtils.preprocess_jsonnode(jsonNode.get("confirmCode").toString());
        if(!username.equals(userInfo[0])) {
            //not same record
            return RestResponse.validfail("You think you are smart? Don't fool us");
        }
        String prcId = userInfo[1];
        //check confirm code is the same
        if(!accountService.checkConfirmCode(prcId, confirmCode)) {
            return RestResponse.validfail("ConfirmCode error");
        }

        //create folder for user certified images
        String folderPath = "Images/CreditAudit/" + userInfo[1]; // Replace with your desired folder path
        FileUtils.create_folder(folderPath);
        folderPath = folderPath + "/";
        FileUtils.save_image(folderPath,IdCardFile, salaryCard, addressProof, taxDocument);

        //send audit information to kafka for credit service to handle
        //msg format: "prcId folder_file_pic pinNum"
        String msg = prcId + " " + folderPath + " " + pin_num;
        ProduceMessageDto produceMessageDto = new ProduceMessageDto();
        List<Integer> partitionList = new ArrayList<>();
        partitionList.add(Constants.kafka_credit_partition);
        produceMessageDto.setValue(msg);
        produceMessageDto.setTopic(Constants.kafka_credit_topic);
        produceMessageDto.setPartition(partitionList);
        kafkaFeign.sendMessage(produceMessageDto);


        return RestResponse.success();
    }

    @GetMapping("/getCardInfo")
    @ResponseBody
    public RestResponse getCardInfo() {
        String[]userInfo = get_token_user();
        if(userInfo == null) {
            return RestResponse.validfail("Please Log in");
        }
        String username = userInfo[0];
        String prcId = userInfo[1];
        try {
            List<CardInfoDto> cardInfoDtos = accountService.getCardInfo(prcId, username);
            return RestResponse.success(blurCardNum(cardInfoDtos));
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail(e.getMessage());
        }
    }

    public List<CardInfoDto> blurCardNum(List<CardInfoDto> cardInfoDtos) {
        for(CardInfoDto cardInfoDto: cardInfoDtos) {
            String cardNum = cardInfoDto.getCardNum();
            cardInfoDto.setCardNum(cardNum.substring(0,6) + "***" + cardNum.substring(13));
        }
        return cardInfoDtos;
    }

    @PostMapping("/transfer")
    @ResponseBody
    public RestResponse transfer(@RequestBody TransactionDto transactionDto) {
        String[]userInfo = get_token_user();
        try {
            accountService.transfer(transactionDto.getTransaction(), userInfo[0],userInfo[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.validfail(e.getMessage());
        }
        return RestResponse.success();
    }

    @GetMapping("/getBankAccountById")
    @ResponseBody
    public RestResponse getBankAccountById(@RequestParam("Id") String bank_id){
        String[]userInfo = get_token_user();
        try {
            String redisValue = accountService.getBankAccountById(userInfo[0], Integer.parseInt(bank_id));
            System.out.println(redisValue + "da");
            return RestResponse.success(redisValue);
        } catch (Exception e) {
            return RestResponse.validfail(e.getMessage());
        }

    }
}
