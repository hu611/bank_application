package com.storage.service;

import com.storage.Dto.CardInfoDto;
import com.storage.pojo.CardInfo;

import java.util.List;

public interface AccountService {
    public void openAccount(String prcId, String username, char card_info) throws Exception;


    public void openDebitAccountAfterConfirm(String prcId, String username, String confirm_code, String pin_num) throws Exception;

    public boolean checkConfirmCode(String prcId, String confirmcode) throws Exception;

    public List<CardInfoDto> getCardInfo(String prcId, String username) throws Exception;
}
