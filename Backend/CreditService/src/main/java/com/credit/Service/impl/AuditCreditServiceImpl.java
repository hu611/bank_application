package com.credit.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.credit.Service.AuditCreditService;
import com.credit.mapper.AuditCreditMapper;
import com.credit.pojo.AuditCredit;
import org.springframework.stereotype.Service;

@Service
public class AuditCreditServiceImpl extends ServiceImpl<AuditCreditMapper, AuditCredit> implements AuditCreditService {
}
