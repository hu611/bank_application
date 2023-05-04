package com.storage.Dto;

import lombok.Data;

/**
 * return both card information for credit and debit
 */
@Data
public class CardInfoDto {
    String cardType;
    String cardNum;
    int id;
}
