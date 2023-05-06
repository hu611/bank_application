package com.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.storage.service.utils.UsefulUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    CardInfoMapper cardInfoMapper;

    @Autowired
    CreditCardFeign creditCardFeign;

    @Autowired
    TransactionRecordMapper transactionRecordMapper;

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
        List<String> cardNumList = cardInfoMapper.getCardByPrcId(prcId);
        int i = 0;
        for(String cardNum: cardNumList) {
            CardInfoDto cardInfoDto = new CardInfoDto();
            cardInfoDto.setCardType("Debit Card");
            cardInfoDto.setId(i);
            cardInfoDto.setCardNum(cardNum);
            ret.add(cardInfoDto);
            redis_hm.put(i++,cardNum);
        }
        //get credit card information
        CreditCard creditCard = creditCardFeign.getCreditCard(prcId);
        if(creditCard != null) {
            CardInfoDto cardInfoDto = new CardInfoDto();
            cardInfoDto.setCardNum(creditCard.getCardNo());
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
     * @param username
     * @throws Exception
     */
    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void transfer(String aesString, String username, String prcId) throws Exception {
        JsonNode jsonNode = null;
        String senderBankAccount;
        String recipientBankAccount;
        String transferAmountString;
        try {
            jsonNode = DecryptUtils.aes_decrypt(aesString);
            senderBankAccount = UsefulUtils.get_json_string_by_field(jsonNode,"senderBankAccount");
            recipientBankAccount = UsefulUtils.get_json_string_by_field(jsonNode,"recipientBankAccount");
            transferAmountString = UsefulUtils.get_json_string_by_field(jsonNode,"transferAmount");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while parsing transaction");
        }


        BigDecimal transferAmount = new BigDecimal(transferAmountString);
        //check if recipient exists
        CardInfo cardInfo = cardInfoMapper.selectById(recipientBankAccount);
        if(cardInfo == null) {
            throw new RuntimeException("Recipient does not exist");
        }

        //insert into transaction record
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTransactionType("Transfer");
        transactionRecord.setTransactionDate(LocalDate.now());
        transactionRecord.setEncodedTransaction(aesString);
        transactionRecord.setTransactionResult("Ongoing");
        int insert = transactionRecordMapper.insert(transactionRecord);
        if(insert == 0) {
            throw new RuntimeException("Error inserting to transaction record, This might due to mysql server problem " +
                    "please try again later");
        }

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
        emailThread.start();


    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void _transfer(String senderBankAccount, String recipientBankAccount, BigDecimal transferAmount) throws Exception {

        CardInfo senderCard = cardInfoMapper.selectCardByCardIdForUpdate(senderBankAccount);
        CardInfo recipientCard = cardInfoMapper.selectCardByCardIdForUpdate(recipientBankAccount);
        BigDecimal currSenderBalance = senderCard.getBalance();
        BigDecimal currRecipientBalance = recipientCard.getBalance();

        //not enough balance
        if(currSenderBalance.compareTo(transferAmount) == -1) {
            throw new RuntimeException("User does not have enough balance");
        }

        senderCard.setBalance(currSenderBalance.subtract(transferAmount));
        recipientCard.setBalance(currRecipientBalance.add(transferAmount));

        int res = cardInfoMapper.updateById(senderCard);
        int res2 = cardInfoMapper.updateById(recipientCard);
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
}
