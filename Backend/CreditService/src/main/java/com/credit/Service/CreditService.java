package com.credit.Service;

import com.base.pojo.CreditCard;

import java.math.BigDecimal;

public interface CreditService {
    public float getCreditScore() throws Exception;

    public BigDecimal[] getLowestPayBackAmount(CreditCard creditCard);

    public BigDecimal getCreditCardCompoundInterest(String prcId) throws Exception;

    public boolean hasCreditCard(String prcId) throws Exception;

    public CreditCard getCreditCardByPrcId(String prcId) throws Exception;

}
