package com.audit.service;

import com.audit.dto.CreditAuditResponse;
import com.audit.mapper.AuditCreditMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    AuditCreditMapper auditCreditMapper;
    @Override
    public List<CreditAuditResponse> getCreditAudit() {
        return auditCreditMapper.getAllAuditCredits();
    }
}
