package com.storage;

import com.base.util.EmailUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.storage.mapper.UserNotificationMapper;
import com.storage.pojo.UserNotification;
import com.storage.service.DecryptService;
import com.storage.storage_service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class DepositTest {

    @Autowired
    UserNotificationMapper userNotificationMapper;

    @Autowired
    TransactionService transactionService;

    @Autowired
    DecryptService decryptService;
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
            JsonNode jsonNode = decryptService.aes_decrypt("+9MAKhQ75eno1GkLxf8K7u+3R9V+8j8RNZ2UeZM82GbaahOLeCTTTup6WvJOXTpM5Fx7Uqu9gPDuBWzsyvf6AQ==");
            System.out.println(jsonNode.get("confirmCode"));
            System.out.println(jsonNode.get("amount"));
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

}
