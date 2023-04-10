package com.storage.storage_service.impl;

import com.storage.mapper.BankUserMapper;
import com.storage.storage_service.transaction_service;
import org.springframework.beans.factory.annotation.Autowired;

public class transaction_serviceImpl implements transaction_service {
    @Autowired
    BankUserMapper bankUserMapper;
    @Override
    public void deposit_money(int amount, String username, String card_no) {


    }
}
