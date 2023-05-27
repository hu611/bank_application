package com.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.RestResponse;
import com.base.pojo.CreditCard;
import com.base.pojo.TransactionRecord;
import com.base.util.DecryptUtils;
import com.base.util.EmailUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.storage.Dto.CardInfoDto;
import com.storage.config.ServerConfig;
import com.storage.mapper.CardInfoMapper;
import com.storage.mapper.TransactionRecordMapper;
import com.storage.pojo.CardInfo;
import com.storage.pojo.UserNotification;
import com.storage.mapper.UserNotificationMapper;
import com.storage.service.AccountService;
import com.storage.service.feign.CreditCardFeign;
import com.storage.service.utils.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import com.storage.service.utils.UsefulUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    UserNotificationMapper userNotificationMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    @Qualifier(value = "ObjectRedisTemplate")
    RedisTemplate objectRedisTemplate;

    @Autowired
    CardInfoMapper cardInfoMapper;

    @Autowired
    CreditCardFeign creditCardFeign;

    @Autowired
    TransactionRecordMapper transactionRecordMapper;

    @Override
    public void batch_insert(List<CardInfo> cardInfoList) {
        batch_insert_debit_balance_redis(cardInfoList);
        batch_insert_card_pinNum_redis(cardInfoList);
        batch_insert_user_cardno_redis(cardInfoList);
    }

    public void batch_insert_card_pinNum_redis(List<CardInfo> cardInfoList) {
        System.out.println("batch insert card pin number started");
        // 获取Redis连接
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

        // 开启管道
        connection.openPipeline();

        for(CardInfo cardInfo: cardInfoList) {
            String cardKey = UsefulUtils._get_redis_pinNum(cardInfo.getCardNo());
            connection.set(serializer.serialize(cardKey), serializer.serialize(cardInfo.getPinNum()));
        }

        connection.closePipeline();
        System.out.println("batch insert card pin number is successful");
    }

    /**
     * 批量插入借记卡数据进入redis
     * e.g key:debit_balance_622203412900799 value:1000.000000
     * @param cardInfoList
     */
    public void batch_insert_debit_balance_redis(List<CardInfo> cardInfoList) {
        System.out.println("batch insert started");
        // 获取Redis连接
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

        // 开启管道
        connection.openPipeline();

        // 执行批量插入操作
        for (CardInfo cardInfo: cardInfoList) {
            String key = UsefulUtils._get_redis_debit_balance_key(cardInfo.getCardNo());
            System.out.println("inserting redis debit balance key:" + key);
            String value = cardInfo.getBalance().toString();
            // 将命令添加到管道中
            connection.set(serializer.serialize(key), serializer.serialize(value));
        }

        // 提交管道命令
        connection.closePipeline();
        System.out.println("batch insert ended");
    }

    /**
     * eg: key: prcid_bankNo_320202020203 value:"622203412900799 622203518628839"
     * @param cardInfoList
     */
    public void batch_insert_user_cardno_redis(List<CardInfo> cardInfoList) {
        // 获取Redis连接
        RedisConnection connection = objectRedisTemplate.getConnectionFactory().getConnection();
        HashSet<String> hashSet = new HashSet<>();
        connection.openPipeline();

        for(CardInfo cardInfo: cardInfoList) {
            CardInfoDto cardInfoDto = new CardInfoDto();
            BeanUtils.copyProperties(cardInfo,cardInfoDto);
            String key = UsefulUtils._get_redis_prcid_bankNo_key(cardInfo.getPrcId());
            if(!hashSet.contains(cardInfo.getPrcId())) {
                objectRedisTemplate.delete(key);
                hashSet.add(cardInfo.getPrcId());
            }
            objectRedisTemplate.opsForList().leftPush(key,cardInfoDto);
            System.out.println("prc id:" + key);
        }

        // 提交管道命令
        connection.closePipeline();
        System.out.println("batch insert ended");

    }

    /**
     * /*
     * This function is responsible for opening an account. It takes a prcId, username, and card_type as parameters and checks
     * if there are too many card numbers, if the customer already has a credit card and if the customer has an email.
     * It then generates a random confirmation code and sets it in the Redis. Finally, a thread is spawned and used to send an email
     * to the customer containing a link with the confirmation code.
     *
     * @param prcId
     * @param username
     * @param card_type
     * @throws Exception
     */
    @Override
    public void openAccount(String prcId, String username, char card_type) throws Exception {
        int cardNo = get_num_debit_card(prcId, card_type);
        if(cardNo > 3) {
            throw new RuntimeException("Too many card numbers");
        }
        if(has_credit_card(prcId) && card_type == '1') {
            throw new RuntimeException("You already have one credit card");
        }
        UserNotification userNotification = userNotificationMapper.selectById(prcId);
        if(userNotification == null || userNotification.getEmail() == null || userNotification.getEmail().length() == 0) {
            throw new RuntimeException("User Email Does not exist");
        }
        String new_url = "";
        if(card_type == '0') {
            new_url = ServerConfig.FRONTEND_LOCATION + "/confirmDebit";
        } else {
            new_url = ServerConfig.FRONTEND_LOCATION + "/confirmCredit";
        }
        //send email to user to confirm
        String confirmcode = UsefulUtils._generate_random_num(4);
        new_url = new_url + String.format("?username=%s&confirmcode=%s", username,confirmcode);
        redisTemplate.opsForValue().set(UsefulUtils._get_redis_open_account_code_key(prcId), card_type + confirmcode,
                ServerConfig.OPEN_ACCOUNT_CODE_REDIS_MINUTES, TimeUnit.MINUTES);
        String finalNew_url = new_url;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EmailUtils._send_email(userNotification.getEmail(), "You have requested to open a bank account\n " +
                            "Please click the following link:" + finalNew_url, "Bank Account Open");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    /**
     * openDebitAccountAfterConfirm is used to open a debit account after receiving confirmation through a given process ID,
     * username, confirmation code, and pin number. It checks if the stored confirmation code matches the input code before
     * proceeding to open the account. If they don't match, an error is thrown. Finally, it clears the redisTemplate.
     * @param prcId
     * @param username
     * @param confirm_code
     * @param pin_num
     * @throws Exception
     */
    @Override
    public void openDebitAccountAfterConfirm(String prcId, String username, String confirm_code, String pin_num) throws Exception {
        if(!checkConfirmCode(prcId, confirm_code)) {
            throw new RuntimeException("Confirm code error");
        }
        openDebitAccountAfterConfirm(prcId, pin_num);

        redisTemplate.delete(UsefulUtils._get_redis_open_account_code_key(prcId));
    }

    @Override
    public boolean checkConfirmCode(String prcId, String confirm_code) throws Exception{
        String stored_confirm_code = (String) redisTemplate.opsForValue()
                .get(UsefulUtils._get_redis_open_account_code_key(prcId));
        if(stored_confirm_code == null) {
            return false;
        }
        //first character is card type
        String redis_confirm_code = stored_confirm_code.substring(1);
        if(!redis_confirm_code.equals(confirm_code)) {
            return false;
        }
        return true;
    }

    @Override
    public List<CardInfoDto> getCardInfo(String prcId, String username) throws Exception {
        List<CardInfoDto> ret =  new ArrayList<>();
        HashMap<Integer, String> redis_hm = new HashMap<>();
        //get Debit card information
        String redisKey = UsefulUtils._get_redis_prcid_bankNo_key(prcId);
        ret = objectRedisTemplate.opsForList().range(redisKey, 0, -1);
        int i = 0;
        for(CardInfoDto cardInfoDto: ret) {
            redis_hm.put(i,cardInfoDto.getCardNo());
            cardInfoDto.setId(i++);

        }
        //get credit card information
        CreditCard creditCard = creditCardFeign.getCreditCard(prcId);
        if(creditCard != null) {
            CardInfoDto cardInfoDto = new CardInfoDto();
            cardInfoDto.setCardNo(creditCard.getCardNo());
            cardInfoDto.setCardType("Credit Card");
            cardInfoDto.setId(i);
            ret.add(cardInfoDto);
            redis_hm.put(i, creditCard.getCardNo());
        }

        //store id CardNum combination to redis
        Thread redisThread = new RedisThread(redis_hm, username);
        redisThread.start();

        return ret;
    }

    public class RedisThread extends Thread {
        HashMap<Integer, String> redis_hm;
        String username;

        public RedisThread(HashMap<Integer, String> redis_hm, String username) {
            this.redis_hm = redis_hm;
            this.username = username;
        }
        @Override
        public void run() {
            Iterator<Map.Entry<Integer,String>> iterator = redis_hm.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> curr = iterator.next();
                String usernameKey = UsefulUtils._get_redis_bank_account_key(curr.getKey(),this.username);
                redisTemplate.opsForValue().set(usernameKey,curr.getValue()
                        ,ServerConfig.BANK_NUMBER_REDIS_MINUTES, TimeUnit.MINUTES);
                System.out.println("storing key: " + usernameKey + " value: " + curr.getValue() + " to redis server");
            }
            super.run();
        }
    }

    public void openDebitAccountAfterConfirm(String prcId,
                                             String pin_num) throws Exception {
        String bank_account_num = generate_bank_account(prcId);
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNo(bank_account_num);
        cardInfo.setBalance(new BigDecimal(0));
        cardInfo.setOpeningDate(LocalDate.now());
        cardInfo.setPinNum(pin_num);
        cardInfo.setPrcId(prcId);
        cardInfo.setCardType("0");
        cardInfoMapper.insert(cardInfo);
        redisTemplate.opsForValue().set(UsefulUtils._get_redis_debit_balance_key(cardInfo.getCardNo())
                ,cardInfo.getBalance());

    }

    public String generate_bank_account(String prcId) {
        String res = ServerConfig.BANK_ACCOUNT_NUMBER_PREFIX + prcId.substring(prcId.length()-3);
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 9; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        res = res + stringBuilder.toString();
        res = res + calculate_final_bank_account_no_digit(res);
        return res;
    }

    public char calculate_final_bank_account_no_digit(String currbankAccount) {
        int first_step_calc = 0;
        int index = 0;
        int second_step_calc = 0;
        for(int i = currbankAccount.length()-1; i >= 0; i--) {
            int curr_digit = currbankAccount.charAt(i)-'0';
            if(index % 2 == 0) {
                int tmp = curr_digit * 2;
                first_step_calc += tmp/10 + tmp%10;
            } else {
                second_step_calc += curr_digit;
            }
            index++;
        }
        int res = 10 - ((first_step_calc+second_step_calc) % 10);
        return (char) res;
    }

    public int get_num_debit_card(String prcId, char card_type) {
        Map<String, Object> map = new HashMap<>();
        map.put("prcId",prcId);
        map.put("card_type",card_type);
        return cardInfoMapper.count_card_no_by_prcId(map);
    }

    public boolean has_credit_card(String prcId) throws Exception {
        return creditCardFeign.hasCreditCard(prcId);
    }

    /**
     * A function for transfer money from sender to recipient
     * @param aesString
     * @throws Exception
     */
    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void transfer(String aesString, String prcId) throws Exception {
        JsonNode jsonNode = null;
        String senderBankAccount;
        String recipientBankAccount;
        String transferAmountString;
        String senderPinNum;
        try {
            jsonNode = DecryptUtils.aes_decrypt(aesString);
            senderBankAccount = UsefulUtils.get_json_string_by_field(jsonNode,"senderBankAccount");
            recipientBankAccount = UsefulUtils.get_json_string_by_field(jsonNode,"recipientBankAccount");
            transferAmountString = UsefulUtils.get_json_string_by_field(jsonNode,"transferAmount");
            senderPinNum = UsefulUtils.get_json_string_by_field(jsonNode,"senderPinNum");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while parsing transaction");
        }

        if(!check_pinNum_correct(senderBankAccount,senderPinNum)) {
            //pin number is not correct, throw error
            throw new RuntimeException("The Pin Number is not correct");
        }

        BigDecimal transferAmount = new BigDecimal(transferAmountString);

        //insert into transaction record
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTransactionType("Transfer");
        transactionRecord.setTransactionDate(LocalDate.now());
        transactionRecord.setEncodedTransaction(aesString);
        int insert = transactionRecordMapper.insert(transactionRecord);
        if(insert == 0) {
            throw new RuntimeException("Error inserting to transaction record, This might due to mysql server problem " +
                    "please try again later");
        }

        String recipientKey = UsefulUtils._get_redis_debit_balance_key(recipientBankAccount);
        String senderKey = UsefulUtils._get_redis_debit_balance_key(senderBankAccount);
        System.out.println("recipient key" + recipientKey);
        if(!redisTemplate.hasKey(recipientKey)) {
            throw new RuntimeException("Recipient does not exist");
        }

        //设置分布式锁
        //设置sender的锁，并且在redis实现转账
        String lock_sender_key = UsefulUtils._get_redis_shared_lock(senderBankAccount);
        acquire_key(redisTemplate, lock_sender_key);
        BigDecimal sender_amount = new BigDecimal(redisTemplate.opsForValue().get(senderKey).toString());
        if(sender_amount.compareTo(transferAmount) < 0) {
            throw new RuntimeException("用户没有足够的转账金额");
        }
        redisTemplate.opsForValue().set(senderKey, sender_amount.subtract(transferAmount).toString());

        //设置recipient的锁
        String lock_recipient_key = UsefulUtils._get_redis_shared_lock(recipientBankAccount);
        RedisUtils.redis_update_balance(redisTemplate,lock_recipient_key,recipientBankAccount, transferAmount);


        Thread sqlThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //finish transferring logic
                try {
                    _transfer(senderBankAccount, recipientBankAccount, transferAmount);
                    transactionRecord.setTransactionType("Success");
                    transactionRecordMapper.updateById(transactionRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                    transactionRecord.setTransactionResult(e.getMessage());
                    transactionRecordMapper.updateById(transactionRecord);
                    return;
                }
            }
        });

        //create a thread to send Email to sender
        Thread emailThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //send Email to sender
                    String senderEmail = userNotificationMapper.selectById(prcId).getEmail();
                    String msg = "You have successfully sent " + transferAmount +
                            "to bank account:" + recipientBankAccount + "\n" + "Please checkout if you have time";
                    EmailUtils._send_email(senderEmail, msg, "Regards to your recent transfer from Bank Personal Project");
                } catch (Exception e) {
                    System.out.println("Send Email Failure");
                }
            }
        });
        sqlThread.start();
        emailThread.start();


    }

    public boolean check_pinNum_correct(String accountNum, String pinNum) {

        String key = UsefulUtils._get_redis_pinNum(accountNum);
        try {
            String realPinNum = redisTemplate.opsForValue().get(key).toString();
            if(realPinNum.equals(pinNum)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void acquire_key(RedisTemplate redisTemplate, String key) throws Exception{
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        boolean acquired = redisConnection.setNX(key.getBytes(StandardCharsets.UTF_8), new byte[0]);
        int i = 0;
        while(!acquired) {
            i++;
            acquired = redisConnection.setNX(key.getBytes(StandardCharsets.UTF_8), new byte[0]);
            if(i > 10) {
                //retry 10 times
                throw new RuntimeException("Failed to obtain locks");
            }
        }

        redisConnection.pExpire(key.getBytes(),100);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void _transfer(String senderBankAccount, String recipientBankAccount, BigDecimal transferAmount) throws Exception {



        int res = cardInfoMapper.updateBalanceByBankNo(transferAmount.toString(), recipientBankAccount);
        int res2 = cardInfoMapper.updateBalanceByBankNo(transferAmount.multiply(new BigDecimal("-1")).toString(), senderBankAccount);
        if(res == 0 || res2 == 0) {
            throw new RuntimeException("Bank Account Update Failure");
        }
    }

    @Override
    public String getBankAccountById(String username, int bank_id) throws Exception {
        String redis_key = UsefulUtils._get_redis_bank_account_key(bank_id,username);
        Object res = redisTemplate.opsForValue().get(redis_key);
        if(res == null) {
            throw new RuntimeException("Please try again later");
        }
        return res.toString();
    }

    @Override
    public CardInfo getDetailedBankAccountInfo(String bank_id, String[]userInfo) throws Exception {
        String bankAccount = redisTemplate.opsForValue().
                get(UsefulUtils._get_redis_bank_account_key(Integer.parseInt(bank_id), userInfo[0])).toString();
        if(bankAccount == null) {
            throw new RuntimeException("No cache in redis, Please try again");
        }
        CardInfo ret = cardInfoMapper.selectById(bankAccount);
        if(ret == null) {
            throw new RuntimeException("No corresponding card inside database. This should not happen.");
        }
        return ret;

    }

}
