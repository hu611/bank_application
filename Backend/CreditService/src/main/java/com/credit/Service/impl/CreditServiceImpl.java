package com.credit.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.credit.Utils.Constants;
import com.credit.Utils.UsefulFunctions;
import com.credit.mapper.*;
import com.base.pojo.*;
import com.credit.Service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
public class CreditServiceImpl implements CreditService {
    @Autowired
    CreditCardBillMapper creditCardBillMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    @Override
    public CreditCard getCreditCardByPrcId(String prcId) throws Exception {
        return creditCardMapper.getCreditCardByPrcId(prcId);
    }

    @Autowired
    CreditCardBillPaybackRecordMapper creditCardBillPaybackRecordMapper;

    @Override
    public float getCreditScore() throws Exception {
        return 0;
    }

    /**
     * 获得最低还款额
     * @param CreditCard
     * @return BigDecimal: 0.信用额度内消费金额×10% 1.预借现金交易金额×100% 2.前期最低还款额未还部分
     * 3.所有费用和利息×100% 4.最新的lowest payback amount
     */
    @Override
    public BigDecimal[] getLowestPayBackAmount(CreditCard creditCard) {
        BigDecimal[] ret = new BigDecimal[5];
        BigDecimal res = new BigDecimal(0);

        //信用额度内消费金额×10%
        BigDecimal balance_rate = creditCard.getBalance().multiply(new BigDecimal(0.1));
        ret[0] = balance_rate;
        res = res.add(balance_rate);

        //预借现金交易金额×100%
        BigDecimal cashAdvance_rate = creditCard.getCashAdvance();
        ret[1] = cashAdvance_rate;
        res = res.add(cashAdvance_rate);

        //前期最低还款额未还部分
        BigDecimal UnpaidMinRepayment_rate = creditCard.getUnpaidMinRepayment().add(creditCard.getUnpaidMinRepayment());
        ret[2] = UnpaidMinRepayment_rate;
        res = res.add(UnpaidMinRepayment_rate);

        //所有费用和利息×100%
        BigDecimal interestAmount_rate = creditCard.getInterestAmount();
        res = res.add(interestAmount_rate);
        ret[3] = interestAmount_rate;
        ret[4] = res;
        return ret;
    }

    /**
     * This will only return this month's compound interest for all unpaid bills
     *
     * @param prcId
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getCreditCardCompoundInterest(String prcId) throws Exception {
        List<CreditCardBill> creditCardUnpaidBills = creditCardBillMapper.getCreditCardBillByPrcIdAndType(prcId, 0);
        CreditCard creditCard = creditCardMapper.getCreditCardByPrcId(prcId);
        BigDecimal res = new BigDecimal(0);
        for (CreditCardBill creditCardBill : creditCardUnpaidBills) {
            LocalDate owe_date = creditCardBill.getOweDate();
            if (owe_date.isAfter(creditCard.getLastBillDate())) {
                //免息日期范围内
                break;
            }
            //获得信用卡还钱记录
            List<CreditCardBillPaybackRecord> creditCardBillPaybackRecords
                    = creditCardBillPaybackRecordMapper.getRecordByBillIdBetweenDate(creditCardBill.getBillId()
                    , creditCard.getLastBillDate(), creditCard.getLastBillDate().plusMonths(1));

            BigDecimal[] bigDecimals = get_interest_amount(creditCardBill.getOweAmount(),
                    creditCardBillPaybackRecords, creditCard.getLastBillDate(), creditCard.getLastBillDate().plusMonths(1));
            res = res.add(bigDecimals[0]);

            if (owe_date.isAfter(creditCard.getLastBillDate().minusMonths(1))) {
                //上个周期是免息范围内，并且没有在这个周期付清
                //额外算前面因为免息没有算的利息

                //上个月的账单还款记录
                LocalDate start_date = creditCard.getLastBillDate().minusMonths(1);
                LocalDate end_date = creditCard.getLastBillDate();
                creditCardBillPaybackRecords
                        = creditCardBillPaybackRecordMapper.getRecordByBillIdBetweenDate(creditCardBill.getBillId(),
                        start_date, end_date);
                BigDecimal[] previous_month = get_interest_amount(bigDecimals[1],
                        creditCardBillPaybackRecords,start_date,end_date);
                res = res.add(previous_month[0]);
            }

        }
        return res;
    }

    /**
     * 因为一个月内可能会多次还钱，所以需要请求他的记录并且分开算,用于审计
     * @param owe_amount: end_date的时候当前bill欠钱总数
     * @param creditCardBillPaybackRecords date followed by desc
     * @param start_date
     * @param end_date
     * @return
     */
    public BigDecimal[] get_interest_amount(BigDecimal owe_amount,
                                          List<CreditCardBillPaybackRecord> creditCardBillPaybackRecords,
                                          LocalDate start_date,
                                          LocalDate end_date) {
        BigDecimal res = new BigDecimal(0);
        for(CreditCardBillPaybackRecord creditCardBillPaybackRecord: creditCardBillPaybackRecords) {
            int days = (int)ChronoUnit.DAYS.between(creditCardBillPaybackRecord.getPaybackDate(), end_date);
            BigDecimal add_interest = UsefulFunctions.calculate_interest(days, owe_amount);
            res = res.add(add_interest);
            owe_amount = owe_amount.add(creditCardBillPaybackRecord.getPaybackAmount());
            end_date = creditCardBillPaybackRecord.getPaybackDate();
        }

        int days = (int)ChronoUnit.DAYS.between(end_date,start_date);
        res = res.add(Constants.INTEREST_RATE.multiply(new BigDecimal(days)).multiply(owe_amount));
        return new BigDecimal[]{res,owe_amount};
    }

    @Override
    public boolean hasCreditCard(String prcId) throws Exception {
        CreditCard creditCard = creditCardMapper.getCreditCardByPrcId(prcId);
        return creditCard != null;
    }
}


