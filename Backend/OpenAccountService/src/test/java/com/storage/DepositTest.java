package com.storage;

import com.base.util.EmailUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.storage.mapper.CardInfoMapper;
import com.storage.mapper.UserNotificationMapper;
import com.storage.pojo.UserNotification;
import com.storage.service.AccountService;
import com.storage.service.DecryptService;
import com.storage.service.TransactionService;
import com.storage.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class DepositTest {

    @Autowired
    UserNotificationMapper userNotificationMapper;

    @Autowired
    TransactionService transactionService;

    @Autowired
    DecryptService decryptService;

    @Autowired
    CardInfoMapper cardInfoMapper;

    @Autowired
    AccountServiceImpl accountService;
    @Test
    public void test_deposit() {
        try {
            EmailUtils._send_email("AustinHu0802@gmail.com", "Hello from Java", "llll");
            transactionService.deposit_money(new BigDecimal(200), "ad23", "12344",
                    "11111111", "KzlNQUtoUTc1ZW5vMUdrTHhmOEs3dSszUjlWKzhqOFJOWjJVZVpNODJHWnkyam91dDd1MGU1NmdEdUdxT2YvRg==");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_aes_decryption() {
        try {
            JsonNode jsonNode = decryptService.aes_decrypt("UYXL+AWJk8Y6p9hSzBrtJcYJ+cnjLdSkBDib3+G9wGTSzQ7OLqhCxDe2lmeeVvBlVDlb5xysn57BLiRfirl8rA==");
            System.out.println(jsonNode.get("confirmCode"));
            System.out.println(jsonNode.get("pinNum"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_big_decimal() {
        try {
            JsonNode jsonNode = decryptService.aes_decrypt("+9MAKhQ75eno1GkLxf8K7u+3R9V+8j8RNZ2UeZM82GbaahOLeCTTTup6WvJOXTpM5Fx7Uqu9gPDuBWzsyvf6AQ==");
            String amount = jsonNode.get("amount").toString();
            amount = amount.substring(1,amount.length()-1);
            BigDecimal decimal = new BigDecimal(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Test
    public void test_user_notification() {
            UserNotification userNotification = userNotificationMapper.selectById("12344");
            System.out.println(userNotification);
    }

    @Test
    public void test_generate_code() {
        try {
            transactionService.generate_code("12344");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_count_by_prc_id() {
        Map<String, Object> map = new HashMap<>();
        map.put("prcId","12344");
        map.put("card_type","0");
        int count = cardInfoMapper.count_card_no_by_prcId(map);
        System.out.println(count);
    }

    @Test
    public void test_format_str() {
        String new_url = "http://localhost:1111";
        String username = "hu611";
        String confirmcode = "ada";
        new_url = new_url + String.format("?username=%s&confirmcode=%s",username,confirmcode);
        System.out.println(new_url);
    }

    @Test
    public void test_luhn_algorithm() {
        String account_no = "625965087177209";
        char a = accountService.calculate_final_bank_account_no_digit(account_no);
        System.out.println(a);
    }

}
