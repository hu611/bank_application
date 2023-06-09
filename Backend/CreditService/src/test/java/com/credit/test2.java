package com.credit;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.pojo.CreditCardBill;
import com.base.pojo.DailyInterestAmountRecord;
import com.credit.Service.CreditService;
import com.credit.Service.CronSchedulerService;
import com.credit.Service.feign.DebitFeign;
import com.credit.mapper.CreditCardBillMapper;
import com.credit.mapper.CreditCardMapper;
import com.credit.mapper.DailyInterestAmountRecordMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
public class test2 {
    @Autowired
    DailyInterestAmountRecordMapper dailyInterestAmountRecordMapper;

    @Autowired
    CreditCardBillMapper creditCardBillMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    @Autowired
    CronSchedulerService cronSchedulerService;

    @Autowired
    CreditService creditService;

    @Autowired
    DebitFeign debitFeign;




    @Test
    public void test_day_between() {
        long days = ChronoUnit.DAYS.between(LocalDate.of(2022,5,1),
                LocalDate.of(2022,3,1));
        System.out.println(days);
    }

    @Test
    public void test_daily_interest_record_insertion() {
        LambdaQueryWrapper<CreditCardBill> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //get not paid bill
        lambdaQueryWrapper.eq(CreditCardBill::getPaid,0);
        List<CreditCardBill> creditCardBills = creditCardBillMapper.selectList(lambdaQueryWrapper);
    }

    @Test
    public void test_kafka_test() {
        //kafkaFeign.getOffset(Constants.kafka_credit_topic,Constants.kafka_partition);
    }

    @Test
    public void test_credit_card_mapper() {
        BigDecimal bigDecimal = new BigDecimal("200");
        creditCardMapper.updateInterest(bigDecimal, "320202020203");
    }

    @Test
    public void test_late_fee_generation() {
        cronSchedulerService.generate_late_fee();
    }

    @Test
    public void test_debit_feign() {
        System.out.println(debitFeign.test1());
    }

    @Test
    public void test_transfer() {
        boolean res = creditService.creditPay("3nF5qT0IHOV2zSUstxZM5VRyq+GzdeN6gmXpMndtM3xwShguYjxS7TviKPX48wOqAtHCs0yxUw0gKHokPomnlQ==");
        System.out.println(res);
    }
}
