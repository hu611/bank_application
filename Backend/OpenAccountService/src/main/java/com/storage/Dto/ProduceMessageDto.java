package com.storage.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

