package com.storage.storage_service;

import com.storage.service.feign.CreditCardFeign;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test3 {
    @Autowired
    CreditCardFeign creditCardFeign;

    @Test
    public void test_feign_credit() throws Exception{
        creditCardFeign.hasCreditCard("123");
    }
}
