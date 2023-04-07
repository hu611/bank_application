package com.bank.Service.impl;

import com.bank.Service.RegisterService;
import com.bank.mapper.BankUserMapper;
import com.bank.mapper.UserNotificationMapper;
import com.bank.pojo.BankUser;
import com.bank.pojo.UserNotification;
import com.dto.RegisterDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Beans;
import java.time.LocalDate;
import java.util.Date;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    BankUserMapper bankUserMapper;
    @Autowired
    UserNotificationMapper userNotificationMapper;

    @Transactional
    @Override
    public int register(RegisterDto registerDto) {
        BankUser bankUser = new BankUser();
        //add to user notification dataset which stores cell phone and email
        if(_setUserNotification(registerDto) == 0) {
            //if previous step fails, then gg
            return 0;
        }
        BeanUtils.copyProperties(registerDto, bankUser);
        boolean gender = registerDto.getGender().equals("male");
        bankUser.setGender(gender);
        LocalDate currTime = LocalDate.now();
        bankUser.setCreateDate(currTime);
        bankUser.setLastUpdate(currTime);
        int insert = bankUserMapper.insert(bankUser);
        if(insert == 0) {
            throw new RuntimeException("正式表添加失败");
        }
        return insert;
    }

    @Transactional
    int _setUserNotification(RegisterDto registerDto) {
        UserNotification userNotification = new UserNotification();
        BeanUtils.copyProperties(registerDto, userNotification);
        return userNotificationMapper.insert(userNotification);
    }
}
