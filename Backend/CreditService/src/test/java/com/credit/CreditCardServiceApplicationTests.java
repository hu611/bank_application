package com.credit;

import com.credit.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CreditCardServiceApplicationTests {
    @Autowired
    CreditCardBillMapper creditCardBillMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void test_credit_card_mapper() {
        creditCardBillMapper.getCreditCardBillByPrcIdAndType("1",1);
    }

}
