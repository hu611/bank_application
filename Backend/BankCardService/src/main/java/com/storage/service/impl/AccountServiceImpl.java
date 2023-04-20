package com.storage.service.impl;

import com.base.util.EmailUtils;
import com.storage.config.ServerConfig;
import com.storage.mapper.CardInfoMapper;
import com.storage.pojo.CardInfo;
import com.storage.pojo.UserNotification;
import com.storage.mapper.UserNotificationMapper;
import com.storage.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.storage.service.utils.UsefulUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    UserNotificationMapper userNotificationMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CardInfoMapper cardInfoMapper;

    @Override
    public void openDebitAccount(String prcId, String username, char card_type) throws Exception {
        int cardNo = get_num_card(prcId, card_type);
        if(cardNo > 3) {
            throw new RuntimeException("Too many card numbers");
        }
        UserNotification userNotification = userNotificationMapper.selectById(prcId);
        if(userNotification == null || userNotification.getEmail() == null || userNotification.getEmail().length() == 0) {
            throw new RuntimeException("User Email Does not exist");
        }
        String new_url = ServerConfig.FRONTEND_LOCATION + "/confirmOpenAccount";
        //todo send email to confirm
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

    @Override
    public void openAccountAfterConfirm(String prcId, String username, String confirm_code, String pin_num) throws Exception {
        String stored_confirm_code = (String) redisTemplate.opsForValue()
                .get(UsefulUtils._get_redis_open_account_code_key(prcId));
        if(stored_confirm_code == null) {
            throw new RuntimeException("会话过期了，请重新申请");
        }
        char card_type = stored_confirm_code.charAt(0);
        String redis_confirm_code = stored_confirm_code.substring(1);
        if(!redis_confirm_code.equals(confirm_code)) {
            throw new RuntimeException("The confirm code is different from input code");
        }
        if(card_type == '0') {
            openDebitAccountAfterConfirm(prcId, pin_num);
        }
        redisTemplate.delete(UsefulUtils._get_redis_open_account_code_key(prcId));
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

    public int get_num_card(String prcId, char card_type) {
        Map<String, Object> map = new HashMap<>();
        map.put("prcId",prcId);
        map.put("card_type",card_type);
        return cardInfoMapper.count_card_no_by_prcId(map);
    }
}
