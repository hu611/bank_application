package com.credit;

import com.base.pojo.CreditCard;
import com.base.pojo.CreditCardBillPaybackRecord;
import com.credit.Service.CreditService;
import com.credit.Service.CronSchedulerService;
import com.credit.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class CreditCardServiceApplicationTests {

    @Autowired
    CronSchedulerService cronSchedulerService;

    @Autowired
    CreditService creditService;

    @Autowired
    CreditCardBillMapper creditCardBillMapper;

    @Autowired
    CreditCardBillPaybackRecordMapper creditCardBillPaybackRecordMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void test_credit_card_mapper() {
        creditCardBillMapper.getCreditCardBillByPrcIdAndType("1",1);
    }

    @Test
    void test_credit_card_bill_payback_record() {
        LocalDate early = LocalDate.of(2023,4,1);
        LocalDate late = LocalDate.of(2023,5,1);
        List<CreditCardBillPaybackRecord> creditCardBillPaybackRecords =
                creditCardBillPaybackRecordMapper.getRecordByBillIdBetweenDate(1,early,late);
        for(CreditCardBillPaybackRecord cardBillPaybackRecord: creditCardBillPaybackRecords) {
            System.out.println(cardBillPaybackRecord);
        }
    }

    @Test
    void test_credit_service() {
        try {
            String prcId = "123456789012345678";
            creditCardMapper.getCreditCardByPrcId(prcId);
            BigDecimal bigDecimal = creditService.getCreditCardCompoundInterest(prcId);
            System.out.println(bigDecimal.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_big_decimal() {
        BigDecimal bigDecimal = new BigDecimal("0");
        BigDecimal a = bigDecimal.add(new BigDecimal(4));
        System.out.println(a);
    }

    @Test
    void test_cron_service() {
        cronSchedulerService.add_interest_amount();
    }

}
