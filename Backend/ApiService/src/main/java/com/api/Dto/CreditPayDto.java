package com.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreditPayDto {
    public String amount;
    public String cardNum;
}
