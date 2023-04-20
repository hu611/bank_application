package com.storage.service;

public interface AccountService {
    public void openDebitAccount(String prcId, String username, char card_info) throws Exception;


    public void openAccountAfterConfirm(String prcId, String username, String confirm_code, String pin_num) throws Exception;
}
