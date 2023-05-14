package com.storage.service.utils;

import com.storage.service.impl.AccountServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;

public class RedisUtils {
    public static void redis_update_balance(RedisTemplate redisTemplate, String lockKey,
                                            String card_no, BigDecimal amount) throws Exception {
        AccountServiceImpl.acquire_key(redisTemplate, lockKey);
        String balanceKey = UsefulUtils._get_redis_debit_balance_key(card_no);
        //System.out.println(balanceKey);
        Object balanceObject =redisTemplate.opsForValue().get(balanceKey);
        BigDecimal balance = new BigDecimal(balanceObject.toString());
        redisTemplate.opsForValue().set(balanceKey, balance.add(amount).toString());
    }
}
