package com.api.service;

import com.api.Constant;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class ApiServiceImpl implements ApiService {
    @Override
    public String generateApiKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] apiKeyBytes = new byte[Constant.API_KEY_LENGTH];
        secureRandom.nextBytes(apiKeyBytes);
        // 使用 Base64 编码生成的随机字节数组
        String apiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(apiKeyBytes);
        return apiKey;
    }

    @Override
    public void send_api_request(String debitCardInfo, String companyName, String companyDesc, String fileLoc) {
        //todo
        //send message to kafka
        //set record in apiRecord
    }

    @Override
    public void update_api_key(String prcId, int record_id) {
        //todo
        //generate api key and aes encrypt it.
        //insert api key into redis
        //insert api key and prcId into api_key table
        //update record in apiRecord
    }
}
