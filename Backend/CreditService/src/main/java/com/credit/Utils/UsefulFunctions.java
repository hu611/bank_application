package com.credit.Utils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UsefulFunctions {
    public static LocalDate get_free_interest_deadline(LocalDate owe_date) {
        return owe_date.plusMonths(1).withDayOfMonth(1);
    }

    public static BigDecimal calculate_interest(int days, BigDecimal owe_amount) {
        BigDecimal constant_interest_rate = Constants.INTEREST_RATE;
        BigDecimal add_interest = constant_interest_rate.multiply(new BigDecimal(days));
        add_interest = add_interest.multiply(owe_amount);
        return add_interest;
    }

    public static String get_Balance_Redis(String accountNum) {
        return accountNum + "_CreditCard_Balance";
    }
}
