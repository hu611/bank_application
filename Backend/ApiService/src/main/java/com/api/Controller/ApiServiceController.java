package com.api.Controller;

import com.api.Constant;
import com.api.Dto.ProduceMessageDto;
import com.api.service.ApiService;
import com.api.service.InitializeService;
import com.api.service.feign.KafkaFeign;
import com.base.RestResponse;
import com.base.util.DecryptUtils;
import com.base.util.FileUtils;
import com.base.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ApiServiceController implements InitializingBean {

    @Autowired
    ApiService apiService;

    @Autowired
    InitializeService initializeService;

    @Override
    public void afterPropertiesSet() throws Exception {
        //todo
        //send api key inside api table into Redis
        initializeService.insertApiKeyToRedis();
    }

    /**
     * aes String should contain amount, type, apiKey, pinNum, accountNum
     * @param aesString
     * @return
     */
    @PostMapping("/pay")
    public RestResponse payWithApiKey(@RequestParam("request") String aesString) {
        return null;
    }

    /**
     * Send message to message queue. The pattern is debitcardnumber companyname company_description file location
     * @param companyCredentialFile
     * @param aesCompanyInfo
     * @return
     * @throws Exception
     */
    @PostMapping("/apiRegistration")
    @ResponseBody
    public RestResponse apiRegistration(@RequestPart("companyCredentialFile") MultipartFile companyCredentialFile,
                                        @RequestParam("companyInfo") String aesCompanyInfo) throws Exception {
        String[]userInfo = get_token_user();
        //create folder for user certified images
        String folderPath = "Images/ApiAudit/" + userInfo[1]; // Replace with your desired folder path
        FileUtils.create_folder(folderPath);
        folderPath = folderPath + "/";
        FileUtils.save_image(folderPath,companyCredentialFile);
        JsonNode jsonNode = DecryptUtils.aes_decrypt(aesCompanyInfo);
        String debitCardInfo = JsonUtils.json_to_string(jsonNode,"debitCardInfo");
        String companyName = JsonUtils.json_to_string(jsonNode,"companyName");
        String companyDesc = JsonUtils.json_to_string(jsonNode, "companyDescription");

        apiService.send_api_request(userInfo[1], debitCardInfo, companyName, companyDesc, folderPath);
        return RestResponse.success();

    }
    public String[] get_token_user() {
        Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principalObj instanceof String) {
            //authentication=username prcID
            String authentication = principalObj.toString();
            String[] userInfo = authentication.split(" ");
            return userInfo;
        }
        return null;
    }
}
