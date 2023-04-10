package com.storage.Dto;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
public class TransactionDto {
    String transaction;
    String card_no;
}
