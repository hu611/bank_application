package com.storage.Dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * return both card information for credit and debit
 */
@Data
@ToString
public class CardInfoDto implements Serializable {
    String cardType;
    String cardNo;
    int id;
}
