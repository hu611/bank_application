package com.storage;

import com.base.RestResponse;
import com.base.pojo.CardInfo;
import com.base.pojo.DebitPlan;
import com.storage.Controller.DebitPlanController;
import com.storage.Dto.CardInfoDto;
import com.storage.mapper.DebitPlanMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest
public class DebitPlanTest {

    @Autowired
    @Qualifier(value = "ObjectRedisTemplate")
    RedisTemplate redisTemplate;

    @Autowired
    DebitPlanMapper debitPlanMapper;

    @Autowired
    DebitPlanController debitPlanController;

    @Test
    public void testAllEffectivePlan() {
        System.out.println(debitPlanMapper.getAllEffectiveDebitPlan());
    }

    @Test
    public void testDebitPlanController() {
        RestResponse restResponse = debitPlanController.getAllPlans();
        return;
    }

    @Test
    public void testSetObjectRedisTemplate() {
        CardInfoDto cardInfoDto = new CardInfoDto();
        cardInfoDto.setCardNo("1333");
        cardInfoDto.setCardType("VISA");
        cardInfoDto.setId(1);
        redisTemplate.opsForList().leftPush("cardInfoList", cardInfoDto);
        cardInfoDto.setCardNo("1555");
        redisTemplate.opsForList().leftPush("cardInfoList", cardInfoDto);
    }

    @Test
    public void getSetObjectRedisTemplate() {
        List<CardInfoDto> cardInfoList = redisTemplate.opsForList().range("prcid_bankNo_005",0,-1);
        for(CardInfoDto cardInfoDto: cardInfoList) {
            System.out.println(cardInfoDto);
        }
    }
}
