package com.credit.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.pojo.CreditCard;
import com.base.pojo.CreditCardBill;
import com.base.pojo.DailyInterestAmountRecord;
import com.base.util.EmailUtils;
import com.credit.Constants;
import com.credit.Service.AuditCreditService;
import com.credit.Service.CreditService;
import com.credit.Service.CronSchedulerService;
import com.credit.Service.feign.KafkaFeign;
import com.credit.Utils.UsefulFunctions;
import com.credit.mapper.CreditCardBillMapper;
import com.credit.mapper.CreditCardMapper;
import com.credit.mapper.DailyInterestAmountRecordMapper;
import com.credit.pojo.AuditCredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class CronSchedulerServiceImpl implements CronSchedulerService{

    @Autowired
    CreditCardBillMapper creditCardBillMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    @Autowired
    DailyInterestAmountRecordMapper dailyInterestAmountRecordMapper;

    @Autowired
    KafkaFeign kafkaFeign;

    @Autowired
    CreditService creditService;

    @Autowired
    AuditCreditService auditCreditService;


    /**每天0点0小时0分开始结算今天
     *错误想法：这部分可以优化，让数据库分开存储这个周期欠的钱和前面周期欠的钱。
     * 错误原因：每个账单的日期都不一样，如果在20号的时候，需要计算账单日期与当前日期经过的天数
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void add_interest_amount() {
        System.out.println("=========Adding daily interest amount=========");
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
        //late fee
    }

    /**
     * 每20分钟处理来自kafka的消息队列，负责银行开信用卡
     */
    @Scheduled(cron = "0 0 */20 * * ?")
    @Override
    public void create_bank_account() {
        System.out.println("Starting to process credit card request");

        List<Object> msgList = kafkaFeign.receiveMsg(Constants.kafka_credit_topic, Constants.kafka_partition);
        List<AuditCredit> auditCreditList = new ArrayList<>();

        //get all credit card requests for last 20 minutes
        for(Object obj_msg: msgList) {
            String msg = obj_msg.toString();
            System.out.println("==============msg is " + msg);
            //msg format: "prcId folder_file_pic pinNum"
            String[] msg_split = msg.split(" ");
            if(msg_split.length != 3) {
                System.out.println(obj_msg + "has wrong format");
                //error format from open account server.
                continue;
            }
            String prcId = msg_split[0];
            String folder_file_pic = msg_split[1];
            String pin_num = msg_split[2];
            AuditCredit auditCredit = new AuditCredit();
            auditCredit.setPicFolderLoc(folder_file_pic);
            auditCredit.setPrcId(prcId);
            auditCredit.setPinNum(pin_num);
            auditCreditList.add(auditCredit);
        }

        auditCreditService.saveBatch(auditCreditList);
        //insert completed

    }

    /**
     * 每个月1号触发这个，让他生成月付款报告
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void generate_monthly_checkout() {
        LambdaQueryWrapper<CreditCard> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<CreditCard> creditCardList = creditCardMapper.selectList(lambdaQueryWrapper);

        //create a thread pool to send Email
        ExecutorService emailThreadPool = new ThreadPoolExecutor(10, 12, 2
                , TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());

        for(CreditCard creditCard: creditCardList){
            BigDecimal[] res = creditService.getLowestPayBackAmount(creditCard);
            creditCard.setLastBillDate(LocalDate.now());
            creditCard.setUnpaidMinRepayment(res[4]);
            creditCardMapper.updateById(creditCard);

            emailThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //todo ask for account service for user email
                    String email = "AustinHu0802@gmail.com";
                    String report = "Your monthly report for " + LocalDate.now();
                    report = report + "\n 信用额度内消费金额×10%: " + res[0].toString().substring(0,5);
                    report = report + "\n 预借现金交易金额×100%: " + res[1].toString().substring(0,5);
                    report = report + "\n 前期最低还款额未还部分 " + res[2].toString().substring(0,5);
                    report = report + "\n 所有费用和利息×100%: " + res[3].toString().substring(0,5);
                    report = report + "\n 总共最低交的金额 " + res[4].toString().substring(0,5);
                    try {
                        EmailUtils._send_email(email, report, "Your monthly report");
                        System.out.println("Send report to " + email + " is successful");
                    } catch (Exception e) {
                        System.out.println(email + "sent failure");
                    }
                }
            });


        }

    }

}
