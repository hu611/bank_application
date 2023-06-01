package com.audit.service;

import com.audit.dto.CreditAuditResponse;

import java.util.List;

public interface AuditService {
    public List<CreditAuditResponse> getCreditAudit();

}
