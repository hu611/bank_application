package com.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayTerm {
    String Amount;
    String AccountNum;
    String PinNum;
    String ApiKey;
    int Type;
}
