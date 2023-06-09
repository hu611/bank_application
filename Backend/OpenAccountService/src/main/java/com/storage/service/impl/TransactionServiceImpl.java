package com.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.pojo.TransactionRecord;
import com.storage.Dto.CardInfoDto;
import com.storage.pojo.UserNotification;
import com.base.util.EmailUtils;
import com.storage.mapper.BankUserMapper;
import com.storage.mapper.CardInfoMapper;
import com.storage.mapper.TransactionRecordMapper;
import com.storage.mapper.UserNotificationMapper;
import com.storage.pojo.CardInfo;
import com.storage.service.TransactionService;
import com.storage.service.utils.RedisUtils;
import com.storage.service.utils.UsefulUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.storage.service.utils.UsefulUtils._generate_random_num;
import static com.storage.service.utils.UsefulUtils._get_redis_confirm_code_key;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    BankUserMapper bankUserMapper;

    @Autowired
    CardInfoMapper cardInfoMapper;

    @Autowired
    TransactionRecordMapper transactionRecordMapper;

    @Autowired
    UserNotificationMapper userNotificationMapper;

    @Autowired
    @Qualifier(value = "ObjectRedisTemplate")
    RedisTemplate objectRedisTemplate;

    /**
     * @description store money into the account
     * @param amount
     * @param username
     * @param prcId
     * @param card_no
     * @param encoded_string
     * @throws Exception
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void deposit_money(BigDecimal amount, String username, String prcId,
                              String card_no, String encoded_string) throws Exception {
        //check if user has this account number
        Boolean containsCard = _prcId_has_card_no(prcId, card_no);
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTransactionDate(LocalDate.now());
        transactionRecord.setEncodedTransaction(encoded_string);
        if(!containsCard) {
            transactionRecord.setTransactionType("Failure");
            transactionRecordMapper.insert(transactionRecord);
            throw new RuntimeException("The person does not have association with this card number");
        }
        //deposit money into account

        String lockKey = UsefulUtils._get_redis_shared_lock(card_no);
        RedisUtils.redis_update_balance(redisTemplate,lockKey,card_no,amount);

        //insert into sql database
        Thread sqlThread = new Thread(new Runnable() {
            @Override
            public void run() {
                cardInfoMapper.updateBalanceByBankNo(amount.toString(), card_no);
            }
        });

        //create a new thread for sending email
        Thread emailthread = new Thread(new Runnable() {
            @Override
            public void run() {
                //transaction complete, put transaction record into database
                //send Email
                UserNotification userNotification = userNotificationMapper.selectById(prcId);
                String emailMessage = "You have successfully deposited " + amount + " money";
                try {
                    EmailUtils._send_email(userNotification.getEmail(), emailMessage, "Personal Bank Project Notification");
                } catch (Exception e) {
                    log.error("send email failed");
                    e.printStackTrace();
                }
            }
        });

        sqlThread.start();
        emailthread.start();

        transactionRecord.setTransactionType("Success");
        transactionRecordMapper.insert(transactionRecord);

    }



    public Boolean _prcId_has_card_no(String prcId, String card_no) {
        String redisKey = UsefulUtils._get_redis_prcid_bankNo_key(prcId);
        List<CardInfoDto> cardInfoList = objectRedisTemplate.opsForList().range(redisKey,0,-1);
        for(CardInfoDto cardInfo: cardInfoList) {
            if(cardInfo.getCardNo().equals(card_no)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void generate_code(String prcId) throws Exception{
        UserNotification userNotification = userNotificationMapper.selectById(prcId);
        String random_code = _generate_random_num(4);
        String message = "This is your random code, please save it carefully and do not share with others\n" + random_code;
        if(userNotification == null || userNotification.getEmail() == null) {
            throw new RuntimeException("User does not have Email");
        }

        System.out.println(_get_redis_confirm_code_key(prcId) + "set for redis value");
        redisTemplate.opsForValue().set(_get_redis_confirm_code_key(prcId), random_code,3, TimeUnit.MINUTES);

        //create a new thread for sending email
        Thread emailthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EmailUtils._send_email(userNotification.getEmail(), message, "Your confirmation code");
                } catch (Exception e) {
                    log.error("send email failed");
                    e.printStackTrace();
                }
            }
        });
        emailthread.start();
    }



    @Override
    public void check_code(String username, String confirmCode) throws Exception{
        String code = (String) redisTemplate.opsForValue().get(_get_redis_confirm_code_key(username));
        if(code == null || confirmCode == null) {
            throw new RuntimeException("Code is null or confirm code is null");
        }
        if(!code.equals(confirmCode)) {
            throw new RuntimeException("The message is not equal");
        }
        redisTemplate.delete(_get_redis_confirm_code_key(username));
    }

}
