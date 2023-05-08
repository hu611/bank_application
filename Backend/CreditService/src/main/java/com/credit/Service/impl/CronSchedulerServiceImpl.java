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
import org.springframework.transaction.annotation.Propagation;
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

    //create a thread pool to send Email
    ExecutorService emailThreadPool = new ThreadPoolExecutor(10, 12, 2
            , TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());


    /**每天0点0小时0分开始结算今天
     *错误想法：这部分可以优化，让数据库分开存储这个周期欠的钱和前面周期欠的钱。
     * 错误原因：每个账单的日期都不一样，如果在20号的时候，需要计算账单日期与当前日期经过的天数
     * Repeatable Read 可以直接看那个时间的数据快照
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {}, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void add_interest_amount() {
        Constants.daily_interest_calculating = true;
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

            //update credit card interest amount
            updateInterest(added_interest, creditCardBill.getPrcId());
        }

        Constants.daily_interest_calculating = false;

    }

    /**
     *read uncommitted：这一段不读取数据，所以无所谓，
     * 为什么要单独拉出来呢？ 因为更新完就直接commit可以快点释放
     * @param added_interest
     * @param prcId
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void updateInterest(BigDecimal added_interest, String prcId) {
        creditCardMapper.updateInterest(added_interest, prcId);
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
        Constants.month_checkout_generating = true;

        LocalDate current_time = LocalDate.now();

        LambdaQueryWrapper<CreditCard> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<CreditCard> creditCardList = creditCardMapper.selectList(lambdaQueryWrapper);



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
        Constants.month_checkout_generating = false;
    }

    /**
     * 每月20日计算滞纳金，为什么repeatable read，因为需要保存事务开启的快照
     */
    @Scheduled(cron = "0 0 0 20 * ?")
    @Transactional(rollbackFor = {}, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void generate_late_fee() {
        List<CreditCard> unpaidCreditCardList = creditCardMapper.selectAllUnpaidMinCreditCards();
        for(CreditCard creditCard: unpaidCreditCardList) {
            BigDecimal added_late_fee = creditCard.getUnpaidMinRepayment().multiply(new BigDecimal("0.05"));
            creditCardMapper.updateLateFee(added_late_fee,creditCard.getCardNo());

            //todo replace email with real user Email
            emailThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //todo ask for account service for user email
                    String email = "AustinHu0802@gmail.com";
                    String report = "亲爱的用户，因为您长时间不交信用卡的最低应付金额，所以需要支付额外的滞纳金: "
                            + added_late_fee.toString().substring(0,5) + "请尽快支付";
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

    public CreditCardBill generateCreditCardBill(LocalDate currentTime,
                                                 BigDecimal new_bill_amount,
                                                 String prcId) {
        CreditCardBill creditCardBill = new CreditCardBill();
        creditCardBill.setBillName( currentTime + " Min Repayment Bill");
        creditCardBill.setBillTotal(new_bill_amount);
        creditCardBill.setPrcId(prcId);
        creditCardBill.setOweAmount(new_bill_amount);
        creditCardBill.setOweDate(currentTime);
        creditCardBill.setPaid(0);
        return creditCardBill;
    }

}
