package com.storage.storage_service;

import java.math.BigDecimal;

public interface TransactionService {
    public void deposit_money(BigDecimal amount, String username, String prcId,
                              String card_no, String encoded_string) throws Exception;
    public void generate_code(String prcId) throws Exception;

    public void check_code(String username, String confirmCode) throws Exception;
}
