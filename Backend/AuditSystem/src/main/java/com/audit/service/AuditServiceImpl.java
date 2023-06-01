package com.audit.service;

import com.audit.dto.CreditAuditResponse;
import com.audit.mapper.AuditCreditMapper;
import com.base.pojo.CreditCard;
import com.base.util.DecryptUtils;
import com.base.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
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
