package com.producer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class ProduceMessageDto {
    String topic;
    List<Integer> partition;
    String key;
    String value;
}
