package com.credit.Service;

import java.math.BigDecimal;

public interface CreditService {
    public float getCreditScore() throws Exception;

    public BigDecimal getLowestPayBackAmount(String prcId) throws Exception;

    public BigDecimal getCreditCardCompoundInterest(String prcId) throws Exception;
}
