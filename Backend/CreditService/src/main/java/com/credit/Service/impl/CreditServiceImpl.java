package com.credit.Service.impl;

import com.base.mapper.CreditCardBillMapper;
import com.credit.Service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class CreditServiceImpl implements CreditService {
    @Autowired
    CreditCardBillMapper creditCardBillMapper;

    @Override
    public float getCreditScore() throws Exception {
        return 0;
    }

    @Override
    public BigDecimal getLowestPayBackAmount(String prcId) throws Exception {
        return null;
    }

    @Override
    public BigDecimal getCreditCardCompoundInterest(String prcId) throws Exception {

        return null;
    }
}
