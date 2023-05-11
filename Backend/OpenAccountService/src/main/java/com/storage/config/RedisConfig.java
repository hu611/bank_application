package com.storage.config;

import com.base.pojo.DebitPlan;
import com.base.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class RedisConfig {
    @Bean(name = "StringRedisTemplate")
    @Primary
    public RedisTemplate<String,Object> stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean(name = "DebitPlanRedisTemplate")
    public RedisTemplate<String, DebitPlan> listRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, DebitPlan> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(DebitPlanSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(DebitPlanSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    public RedisSerializer<DebitPlan> DebitPlanSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new RedisSerializer<DebitPlan>() {
            @Override
            public byte[] serialize(DebitPlan debitPlan) throws SerializationException {
                try {
                    String jsonString = JsonUtils._object_to_json(debitPlan).toString();
                    return jsonString.getBytes(StandardCharsets.UTF_8);
                } catch (Exception e) {
                    throw new RuntimeException("Error while serializing debitplan");
                }
            }

            @Override
            public DebitPlan deserialize(byte[] bytes) throws SerializationException {
                try {
                    String newString = new String(bytes, StandardCharsets.UTF_8);
                    JsonNode jsonNode = JsonUtils._string_to_json(newString);
                    DebitPlan debitPlan = new DebitPlan();
                    debitPlan.setPlanId(JsonUtils.json_to_int(jsonNode,"planId"));
                    debitPlan.setPlanTitle(JsonUtils.json_to_string(jsonNode,"planTitle"));
                    debitPlan.setPlanDesc(JsonUtils.json_to_string(jsonNode,"planDesc"));
                    debitPlan.setFreezeAmount(JsonUtils.json_to_int(jsonNode, "freezeAmount"));
                    debitPlan.setInterestRate(JsonUtils.json_to_float(jsonNode,"interestRate"));
                    debitPlan.setDurationMonth(JsonUtils.json_to_int(jsonNode, "durationMonth"));
                    debitPlan.setExpireDate(LocalDate.now());
                    debitPlan.setStartDate(LocalDate.now());
                    return debitPlan;
                } catch (Exception e) {
                    throw new RuntimeException("Error while deserializing");
                }
            }


        };
    }
}
