package com.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferDto {
    String senderBankAccount;
    String recipientBankAccount;
    String transferAmount;
    String senderPinNum;
}
