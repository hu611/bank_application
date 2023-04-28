package com.storage.service.feign;

import com.storage.Dto.ProduceMessageDto;
import feign.hystrix.FallbackFactory;

public class KafkaFallBackFactory implements FallbackFactory<KafkaFeign> {
    @Override
    public KafkaFeign create(Throwable throwable) {
        return new KafkaFeign() {
            @Override
            public String sendMessage(ProduceMessageDto produceMessageDto) {
                return null;
            }
        };
    }
}
