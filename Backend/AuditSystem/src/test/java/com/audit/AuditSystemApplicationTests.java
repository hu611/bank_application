package com.audit;

import com.audit.dto.CreditAuditResponse;
import com.audit.service.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AuditSystemApplicationTests {

    @Autowired
    AuditService auditService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testAuditService() {
        List<CreditAuditResponse> creditAuditResponses = auditService.getCreditAudit();
        return;
    }

}
