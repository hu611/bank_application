package com.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreditAuditResponse {
    String prcId;
    String applicationTime;
}
