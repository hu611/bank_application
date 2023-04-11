package com.storage;

import com.base.util.EmailUtils;
import com.storage.mapper.UserNotificationMapper;
import com.storage.pojo.UserNotification;
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
    @Test
    public void test_deposit() {
        try {
            EmailUtils._send_email("AustinHu0802@gmail.com", "Hello from Java", "llll");
            transactionService.deposit_money(new BigDecimal(200), "ad23", "12344",
                    "11111111", "dB1j+wlWlbpm2JApjkhBUptuuBuLAOFnmhXf9zAMTG9Je6n1Y9OkwIsKlg5gRyX1");
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
