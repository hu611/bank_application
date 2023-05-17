package com.api.service;

import com.api.Constant;
import com.api.Dto.ProduceMessageDto;
import com.api.mapper.ApiKeyMapper;
import com.api.mapper.ApiRequestRecordMapper;
import com.api.service.feign.KafkaFeign;
import com.base.pojo.ApiKey;
import com.base.pojo.ApiRequestRecord;
import com.base.util.DecryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    KafkaFeign kafkaFeign;

    @Autowired
    ApiRequestRecordMapper apiRequestRecordMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ApiKeyMapper apiKeyMapper;

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
    public void send_api_request(String prcId, String debitCardInfo, String companyName, String companyDesc, String fileLoc) {
        //todo
        //send message to kafka
        List<Integer> partitionList = new ArrayList<>();
        partitionList.add(0);
        String value = debitCardInfo + " " + companyName + " " + companyDesc + " " + fileLoc;
        ProduceMessageDto produceMessageDto = new ProduceMessageDto(Constant.apiService_topic, partitionList, "key",value);
        String result = kafkaFeign.sendMessage(produceMessageDto);
        //set record in apiRecord
        Thread apiRecordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ApiRequestRecord apiRequestRecord = new ApiRequestRecord(null, prcId, companyName, companyDesc, LocalDate.now(), "Pending");
                apiRequestRecordMapper.insert(apiRequestRecord);
            }
        });
        apiRecordThread.start();
    }



    @Override
    public void update_api_key(String prcId, int record_id) {
        //todo
        //generate api key and aes encrypt it.
        String apiKey = generateApiKey();
        String encryptedApiKey = "";
        try {
            encryptedApiKey = DecryptUtils.aes_encrypt_string(apiKey);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while encrypting API key");
        }
        //insert api key into redis
        redisTemplate.opsForValue().set(prcId, encryptedApiKey);
        //insert api key and prcId into api_key table
        ApiKey apiKey1 = new ApiKey(prcId,encryptedApiKey);
        apiKeyMapper.insert(apiKey1);
        //update record in apiRecord
        apiRequestRecordMapper.updateResultById(record_id);
    }
}
