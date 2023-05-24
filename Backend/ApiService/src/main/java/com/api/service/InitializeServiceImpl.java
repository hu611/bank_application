package com.api.service;

import com.api.Constant;
import com.api.mapper.ApiKeyMapper;
import com.base.pojo.ApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitializeServiceImpl implements InitializeService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ApiKeyMapper apiKeyMapper;
    @Override
    public void insertApiKeyToRedis() {
        List<ApiKey> apiKeyList = apiKeyMapper.selectAllApiKey();
        for(ApiKey apiKey: apiKeyList) {
            String redisKey = Constant.getApiKeyRedis(apiKey.getPrcId());
            redisTemplate.opsForValue().set(redisKey, apiKey.getApiKey());
            System.out.println("Inserting " + redisKey + " to Redis");
        }
    }
}
