package com.bank.mapper;

import com.bank.pojo.BankUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-04-03
 */
@Mapper
public interface BankUserMapper extends BaseMapper<BankUser> {
    public BankUser selectByUserName(String username);
}


