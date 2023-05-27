package com.api.service;

import com.api.Constant;
import com.api.mapper.ApiKeyMapper;
import com.base.pojo.ApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
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
        System.out.println("batch insert started");
        // 获取Redis连接
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

        connection.openPipeline();
        List<ApiKey> apiKeyList = apiKeyMapper.selectAllApiKey();
        for(ApiKey apiKey: apiKeyList) {
            String redisKey = Constant.getApiKeyRedis(apiKey.getPrcId());
            //prcId ApiKey
            connection.set(serializer.serialize(redisKey), serializer.serialize(apiKey.getApiKey()));

            //ApiKey AccountNum
            String redisApiKey = Constant.getAccountNumRedis(apiKey.getApiKey());
            connection.set(serializer.serialize(redisApiKey), serializer.serialize(apiKey.getAccountNum()));

            System.out.println("Inserting " + redisApiKey + " to Redis");
        }

        // 提交管道命令
        connection.closePipeline();
        System.out.println("batch insert ended");
    }
}
