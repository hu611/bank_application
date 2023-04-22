package com.credit.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.pojo.CreditCard;
import com.base.pojo.CreditCardBill;
import com.base.pojo.DailyInterestAmountRecord;
import com.credit.Service.CronSchedulerService;
import com.credit.Utils.UsefulFunctions;
import com.credit.mapper.CreditCardBillMapper;
import com.credit.mapper.CreditCardMapper;
import com.credit.mapper.DailyInterestAmountRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CronSchedulerServiceImpl implements CronSchedulerService {

    @Autowired
    CreditCardBillMapper creditCardBillMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    @Autowired
    DailyInterestAmountRecordMapper dailyInterestAmountRecordMapper;


    /**每天0点0小时0分开始结算今天
     *
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void add_interest_amount() {
        LambdaQueryWrapper<CreditCardBill> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //get not paid bill
        lambdaQueryWrapper.eq(CreditCardBill::getPaid,0);
        List<CreditCardBill> creditCardBills = creditCardBillMapper.selectList(lambdaQueryWrapper);
        LocalDate now = LocalDate.now();
        for(CreditCardBill creditCardBill: creditCardBills) {
            BigDecimal added_interest = new BigDecimal(0);
            BigDecimal owe_amount = creditCardBill.getOweAmount();
            LocalDate deadline = UsefulFunctions.get_free_interest_deadline(creditCardBill.getOweDate());
            if(now.isBefore(deadline.plusDays(20))) {
                //免息范围内
                continue;
            }
            if(now.isEqual(deadline.plusDays(20))) {
                //免息结束，但没有还清
                int days = (int) ChronoUnit.DAYS.between(creditCardBill.getOweDate(), now);
                added_interest = UsefulFunctions.calculate_interest(days, owe_amount);
            }
            added_interest = added_interest.add(UsefulFunctions.calculate_interest(1, owe_amount));
            System.out.println(added_interest);
            //=============算完利息==========

            //设置记录
            DailyInterestAmountRecord dailyInterestAmountRecord = new DailyInterestAmountRecord();
            dailyInterestAmountRecord.setInterestAmount(added_interest);
            dailyInterestAmountRecord.setRecordDate(now);
            dailyInterestAmountRecord.setPrcId(creditCardBill.getPrcId());
            dailyInterestAmountRecordMapper.add_record(dailyInterestAmountRecord);

            //更新信用卡
            CreditCard creditCard = creditCardMapper.getCreditCardByPrcId(creditCardBill.getPrcId());
            creditCard.setInterestAmount(creditCard.getInterestAmount().add(added_interest));
            creditCardMapper.updateById(creditCard);
        }
    }
}
