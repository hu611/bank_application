package com.producer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@NoArgsConstructor
@ToString
public class ProduceMessageDto {
    String topic;
    int partition;
    String key;
    String value;
}
