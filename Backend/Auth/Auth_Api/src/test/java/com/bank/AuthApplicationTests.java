package com.bank;


import com.bank.mapper.BankUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthApplicationTests {
    @Autowired
    BankUserMapper bankUserMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void test_bank_mapper() {
        System.out.println(bankUserMapper.selectById(2));
    }

}
